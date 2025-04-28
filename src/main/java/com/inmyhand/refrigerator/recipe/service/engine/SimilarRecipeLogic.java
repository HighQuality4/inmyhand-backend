package com.inmyhand.refrigerator.recipe.service.engine;

import com.inmyhand.refrigerator.recipe.domain.dto.SimilarRecipeDTO;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimilarRecipeLogic {

    private final RecipeInfoRepository recipeInfoRepository;

    // Komoran 형태소 분석기 인스턴스 생성
    private final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    // 캐시 관련 상수 및 변수
    private static final long CACHE_EXPIRY = 3600000; // 1시간마다 캐시 갱신
    private Map<String, Double> idfCache = new ConcurrentHashMap<>();
    private long lastIdfUpdateTime = 0;
    private List<RecipeInfoEntity> allRecipesCache = null;
    private long lastRecipesUpdateTime = 0;

    // TF-IDF 파일 저장 경로
//    private static final String TF_IDF_FILE_PATH = "C:\\code\\inmyhand\\tfidf_cache.ser";

    @Value("${tfidf.file}")
    private String TF_IDF_FILE_PATH;

    // 유사도 계산 방식 (코사인 유사도 기본 사용)
    private boolean useEuclideanDistance = true;

    // 형태소 분석기에서 선택할 품사 리스트
    private static final Set<String> VALID_POS_PREFIXES = new HashSet<>(Arrays.asList(
            "NN",  // 명사
            "VV",  // 동사
            "VA",  // 형용사
            "XR",  // 어근
            "SL"   // 외국어
    ));

    /**
     * 레시피의 재료 정보를 문자열로 결합
     */
    private String combineIngredients(RecipeInfoEntity recipe) {
        if (recipe == null || recipe.getRecipeIngredientList() == null || recipe.getRecipeIngredientList().isEmpty()) {
            return "";
        }

        return recipe.getRecipeIngredientList().stream()
                .map(RecipeIngredientEntity::getIngredientName)
                .collect(Collectors.joining(" "));
    }

    /**
     * 텍스트 토크나이징 (단어 분리) - Komoran 형태소 분석기 적용
     */
    private List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Komoran을 사용한 형태소 분석
            KomoranResult analyzeResult = komoran.analyze(text);

            // 선택된 품사만 필터링
            return analyzeResult.getTokenList().stream()
                    .filter(token -> VALID_POS_PREFIXES.stream()
                            .anyMatch(prefix -> token.getPos().startsWith(prefix)))
                    .map(Token::getMorph)
                    .filter(word -> !word.isEmpty() && word.length() > 1)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Komoran 형태소 분석 중 오류 발생: {}", e.getMessage());

            // 형태소 분석 실패 시 기존 방식으로 폴백
            return Arrays.stream(text.toLowerCase()
                            .replaceAll("[^\\p{IsHangul}\\p{IsAlphabetic}\\p{IsDigit}\\s]", " ")
                            .split("\\s+"))
                    .filter(word -> !word.isEmpty() && word.length() > 1)
                    .collect(Collectors.toList());
        }
    }

    /**
     * TF(Term Frequency) 계산
     */
    private Map<String, Double> computeTF(List<String> terms) {
        if (terms == null || terms.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            Map<String, Double> tf = new HashMap<>();
            double totalTerms = terms.size();

            // 각 단어의 빈도 계산
            terms.forEach(term -> tf.put(term, tf.getOrDefault(term, 0.0) + 1.0));

            // 정규화
            tf.replaceAll((k, v) -> v / totalTerms);

            return tf;
        } catch (Exception e) {
            log.warn("TF 계산 중 오류 발생: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * 모든 레시피 가져오기 (캐싱 적용)
     */
    private List<RecipeInfoEntity> getAllRecipes() {
        long currentTime = System.currentTimeMillis();

        // 캐시가 없는 경우 새로 조회
        if (allRecipesCache == null) {
            try {
                allRecipesCache = recipeInfoRepository.similarFindAll();
                lastRecipesUpdateTime = currentTime;
                log.info("레시피 캐시가 갱신되었습니다. 총 " + allRecipesCache.size() + "개의 레시피");
            } catch (Exception e) {
                log.error("레시피 조회 중 오류 발생: {}", e.getMessage());
                return allRecipesCache != null ? allRecipesCache : Collections.emptyList();
            }
        }

        return allRecipesCache;
    }

    /**
     * IDF(Inverse Document Frequency) 계산 및 파일 캐싱 적용
     */
    private Map<String, Double> computeIDF(Set<String> vocabulary, List<RecipeInfoEntity> allRecipes) {
        if (vocabulary == null || vocabulary.isEmpty() || allRecipes == null || allRecipes.isEmpty()) {
            return Collections.emptyMap();
        }

        long currentTime = System.currentTimeMillis();
        boolean isCacheValid = !idfCache.isEmpty();

        // 캐시가 비어있는 경우, 파일에서 로드 시도
        if (!isCacheValid && idfCache.isEmpty()) {
            loadIdfCache();
        }

        // 캐시에서 가능한 단어 추출
        Map<String, Double> idf = new HashMap<>();
        Set<String> uncachedTerms = new HashSet<>();

        vocabulary.forEach(term -> {
            if (idfCache.containsKey(term)) {
                idf.put(term, idfCache.get(term));
            } else {
                uncachedTerms.add(term);
            }
        });

        // 캐시되지 않은 단어만 계산
        if (!uncachedTerms.isEmpty()) {
            try {
                int totalDocs = allRecipes.size();

                // 각 문서에 나타난 단어 집합 미리 계산
                Map<Long, Set<String>> docTermSets = new HashMap<>();
                allRecipes.forEach(recipe -> {
                    String ingredients = combineIngredients(recipe);
                    if (!ingredients.isEmpty()) {
                        docTermSets.put(recipe.getId(), new HashSet<>(tokenize(ingredients)));
                    }
                });

                // 각 단어별 문서 빈도 계산
                Map<String, Integer> termDocFreq = new HashMap<>();
                uncachedTerms.forEach(term -> {
                    long docsWithTerm = docTermSets.values().stream()
                            .filter(terms -> terms.contains(term))
                            .count();
                    termDocFreq.put(term, (int) docsWithTerm);
                });

                // IDF 계산 및 캐시 저장
                uncachedTerms.forEach(term -> {
                    int docsWithTerm = termDocFreq.getOrDefault(term, 0);
                    double idfValue;

                    if (docsWithTerm == 0) {
                        idfValue = Math.log((double) totalDocs + 1);
                    } else {
                        idfValue = Math.log((double) totalDocs / docsWithTerm);
                    }

                    idfValue = Math.max(0, idfValue);
                    idf.put(term, idfValue);
                    idfCache.put(term, idfValue);
                });

                lastIdfUpdateTime = currentTime;
                log.info("IDF 캐시가 갱신되었습니다. " + uncachedTerms.size() + "개의 새 단어 추가");

                // 파일에 캐시 저장
                saveIdfCache();
            } catch (Exception e) {
                log.warn("IDF 계산 중 오류 발생: {}", e.getMessage());
            }
        }
        return idf;
    }

    /**
     * IDF 캐시를 파일로 저장
     */
    public void saveIdfCache() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(TF_IDF_FILE_PATH)))) {
            oos.writeObject(idfCache);
            log.info("IDF 캐시가 파일에 저장되었습니다: {}", TF_IDF_FILE_PATH);
        } catch (IOException e) {
            log.error("IDF 캐시 파일 저장 중 오류 발생: {}", e.getMessage());
        }
    }


    /**
     * IDF 캐시를 파일에서 로드
     */
    @SuppressWarnings("unchecked")
    public void loadIdfCache() {
        File file = new File(TF_IDF_FILE_PATH);
        if (!file.exists()) {
            log.info("IDF 캐시 파일이 존재하지 않습니다: {}", TF_IDF_FILE_PATH);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                idfCache = (Map<String, Double>) obj;
                lastIdfUpdateTime = System.currentTimeMillis();
                log.info("IDF 캐시가 파일에서 로드되었습니다: {} ({}개 항목)", TF_IDF_FILE_PATH, idfCache.size());
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("IDF 캐시 파일 로드 중 오류 발생: {}", e.getMessage());
        }
    }

    /**
     * 벡터 크기 계산
     */
    private double computeMagnitude(Map<String, Double> vector) {
        if (vector == null || vector.isEmpty()) {
            return 1e-5;
        }

        try {
            double sumOfSquares = vector.values().stream()
                    .filter(val -> !Double.isNaN(val) && !Double.isInfinite(val))
                    .mapToDouble(val -> val * val)
                    .sum();

            // 수치적 안정성
            if (sumOfSquares < 1e-10) {
                return 1e-5;
            }

            return Math.sqrt(sumOfSquares);
        } catch (Exception e) {
            log.warn("벡터 크기 계산 중 오류 발생: {}", e.getMessage());
            return 1e-5;
        }
    }

    /**
     * 레시피 간 유사도 계산
     */
    private double calculateSimilarity(RecipeInfoEntity recipe1, RecipeInfoEntity recipe2) {
        // 입력값 유효성 검사
        if (recipe1 == null || recipe2 == null) {
            log.warn("Null 레시피가 전달되었습니다.");
            return 0.0;
        }

        // 재료 정보 결합
        String ingredients1 = combineIngredients(recipe1);
        String ingredients2 = combineIngredients(recipe2);

        if (ingredients1.isEmpty() || ingredients2.isEmpty()) {
            return calculateTitleSimilarity(recipe1, recipe2);
        }

        try {
            // 1. 텍스트 토큰화
            List<String> terms1 = tokenize(ingredients1);
            List<String> terms2 = tokenize(ingredients2);

            if (terms1.isEmpty() || terms2.isEmpty()) {
                return calculateTitleSimilarity(recipe1, recipe2);
            }

            // 2. 어휘 집합 생성
            Set<String> vocabulary = new HashSet<>();
            vocabulary.addAll(terms1);
            vocabulary.addAll(terms2);

            // 3. TF 계산
            Map<String, Double> tf1 = computeTF(terms1);
            Map<String, Double> tf2 = computeTF(terms2);

            // 4. IDF 계산
            Map<String, Double> idf = computeIDF(vocabulary, getAllRecipes());

            // 5. TF-IDF 벡터 생성
            Map<String, Double> tfidf1 = vocabulary.stream()
                    .collect(Collectors.toMap(
                            term -> term,
                            term -> {
                                double tfValue = tf1.getOrDefault(term, 0.0);
                                double idfValue = idf.getOrDefault(term, 0.0);
                                // 안전 검사
                                if (Double.isNaN(tfValue) || Double.isInfinite(tfValue)) tfValue = 0.0;
                                if (Double.isNaN(idfValue) || Double.isInfinite(idfValue)) idfValue = 0.0;
                                return tfValue * idfValue;
                            }
                    ));

            Map<String, Double> tfidf2 = vocabulary.stream()
                    .collect(Collectors.toMap(
                            term -> term,
                            term -> {
                                double tfValue = tf2.getOrDefault(term, 0.0);
                                double idfValue = idf.getOrDefault(term, 0.0);
                                // 안전 검사
                                if (Double.isNaN(tfValue) || Double.isInfinite(tfValue)) tfValue = 0.0;
                                if (Double.isNaN(idfValue) || Double.isInfinite(idfValue)) idfValue = 0.0;
                                return tfValue * idfValue;
                            }
                    ));
            // 6. 유클리디안 거리 또는 코사인 유사도 계산
            if (useEuclideanDistance) {
                // 유클리디안 거리 계산
                double sumSquaredDiff = vocabulary.stream()
                        .mapToDouble(term -> {
                            double v1 = tfidf1.getOrDefault(term, 0.0);
                            double v2 = tfidf2.getOrDefault(term, 0.0);
                            double diff = v1 - v2;
                            return diff * diff;
                        })
                        .sum();

                // 안정성을 위한 체크
                if (sumSquaredDiff < 1e-10) {
                    log.debug("매우 작은 제곱 차이 합 감지: {}. 기본값 사용", sumSquaredDiff);
                    sumSquaredDiff = 1e-10;
                }

                // 유클리디안 거리
                double distance = Math.sqrt(sumSquaredDiff);

                // 유클리디안 거리를 유사도로 변환 (1 / (1 + 거리))
                double similarity = 1.0 / (1.0 + distance);

                // 검증 및 범위 제한
                if (Double.isNaN(similarity) || Double.isInfinite(similarity) || similarity < 0) {
                    log.warn("유효하지 않은 유클리디안 유사도 값이 계산되었습니다: " + similarity);
                    return calculateTitleSimilarity(recipe1, recipe2);
                }

                return Math.min(1.0, Math.max(0.0, similarity));
            } else {
                // 코사인 유사도 계산
                double dotProduct = vocabulary.stream()
                        .mapToDouble(term -> {
                            double v1 = tfidf1.getOrDefault(term, 0.0);
                            double v2 = tfidf2.getOrDefault(term, 0.0);
                            return v1 * v2;
                        })
                        .sum();

                double magnitude1 = computeMagnitude(tfidf1);
                double magnitude2 = computeMagnitude(tfidf2);

                double denominator = magnitude1 * magnitude2;
                if (denominator < 1e-10) {
                    return calculateTitleSimilarity(recipe1, recipe2);
                }

                double similarity = dotProduct / denominator;

                // 검증 및 범위 제한
                if (Double.isNaN(similarity) || Double.isInfinite(similarity) || similarity < 0) {
                    log.warn("유효하지 않은 코사인 유사도 값이 계산되었습니다: " + similarity);
                    return calculateTitleSimilarity(recipe1, recipe2);
                }

                return Math.min(1.0, Math.max(0.0, similarity));
            }

        } catch (Exception e) {
            log.warn("유사도 계산 중 오류 발생: {}", e.getMessage());
            return calculateTitleSimilarity(recipe1, recipe2);
        }
    }

    /**
     * 제목 기반 유사도 계산 - 자카드 유사도 적용 (Komoran 적용)
     */
    private double calculateTitleSimilarity(RecipeInfoEntity recipe1, RecipeInfoEntity recipe2) {
        if (recipe1 == null || recipe2 == null) {
            return 0.0;
        }

        try {
            if (recipe1.getRecipeName() == null || recipe2.getRecipeName() == null) {
                // ID 기반의 낮은 유사도 점수 반환
                return 0.001 * (1.0 / (Math.abs(recipe1.getId() - recipe2.getId()) + 1));
            }

            // 제목 토큰화 (Komoran 적용)
            List<String> titleTokens1 = tokenize(recipe1.getRecipeName());
            List<String> titleTokens2 = tokenize(recipe2.getRecipeName());

            if (titleTokens1.isEmpty() || titleTokens2.isEmpty()) {
                // ID 기반의 낮은 유사도 점수 반환
                return 0.001 * (1.0 / (Math.abs(recipe1.getId() - recipe2.getId()) + 1));
            }

            // 자카드 유사도 계산 (교집합 / 합집합)
            Set<String> union = new HashSet<>(titleTokens1);
            union.addAll(titleTokens2);

            Set<String> intersection = new HashSet<>(titleTokens1);
            intersection.retainAll(titleTokens2);

            if (union.isEmpty()) {
                return 0.001;
            }

            return (double) intersection.size() / union.size();

        } catch (Exception e) {
            log.warn("제목 유사도 계산 중 오류 발생: {}", e.getMessage());
            return 0.001;
        }
    }

    /**
     * 유사한 레시피 찾기
     */
    public List<RecipeInfoEntity> getSimilarRecipes(Long recipeId, int limit) {
        if (recipeId == null) {
            throw new IllegalArgumentException("레시피 ID는 null일 수 없습니다.");
        }

        if (limit <= 0) limit = 5; // 기본값 설정

        try {
            RecipeInfoEntity targetRecipe = recipeInfoRepository.similarFindById(recipeId)
                    .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다: " + recipeId));

            // 모든 레시피 가져오기 (캐싱 적용)
            List<RecipeInfoEntity> allRecipes = getAllRecipes();

            // 대상 레시피가 없는 경우
            if (allRecipes.isEmpty()) {
                log.warn("레시피가 없습니다.");
                return Collections.emptyList();
            }

            // 병렬 스트림으로 유사도 계산
//            Map<Long, Double> similarityMap = allRecipes.parallelStream()
//                    .filter(recipe -> !recipe.getId().equals(recipeId))
//                    .collect(Collectors.toMap(
//                            RecipeInfoEntity::getId,
//                            recipe -> calculateSimilarity(targetRecipe, recipe)
//                    ));
            // 병렬처리 안함 오류 뜸 -> 하이버네이트 세션 유지 안됨 그러므로 안함
            Map<Long, Double> similarityMap = allRecipes.stream()
                    .filter(recipe -> !recipe.getId().equals(recipeId))
                    .collect(Collectors.toMap(
                            RecipeInfoEntity::getId,
                            recipe -> calculateSimilarity(targetRecipe, recipe)
                    ));

            // 디버깅 로그
            log.info("=== 유사도 계산 결과 (" + recipeId + ") ===");
            allRecipes.stream()
                    .filter(recipe -> !recipe.getId().equals(recipeId))
                    .sorted((p1, p2) -> Double.compare(
                            similarityMap.getOrDefault(p2.getId(), 0.0),
                            similarityMap.getOrDefault(p1.getId(), 0.0)))
                    .limit(Math.min(10, limit * 2)) // 최대 10개까지 로그 출력
                    .forEach(recipe -> {
                        double similarity = similarityMap.getOrDefault(recipe.getId(), 0.0);
                        log.info(String.format("ID: %d, 제목: %s, 유사도: %.4f",
                                recipe.getId(), recipe.getRecipeName(), similarity));
                    });

            // 유사도 높은 순으로 정렬 (내림차순)
            List<RecipeInfoEntity> result = allRecipes.stream()
                    .filter(recipe -> !recipe.getId().equals(recipeId))
                    .sorted((p1, p2) -> Double.compare(
                            similarityMap.getOrDefault(p2.getId(), 0.0),
                            similarityMap.getOrDefault(p1.getId(), 0.0)))
                    .limit(limit)
                    .collect(Collectors.toList());

            return result;

        } catch (Exception e) {
            log.error("유사 레시피 검색 중 오류 발생: {}", e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 오버로딩된 메서드 - 기본 limit 값 사용
     */
    @Transactional(readOnly = true)
    public List<SimilarRecipeDTO> getSimilarRecipes(Long recipeId) {
        return getSimilarRecipes(recipeId, 3).stream()
                .limit(3)
                .map(i -> new SimilarRecipeDTO(i.getId(), i.getRecipeName(), i.getFilesEntities().get(0).getFileUrl()))
                .toList();

//        return recipes.stream()
//                        .limit(3)
//                        .map(i -> i.getId())
//                        .toList();
//        recipes.forEach(recipe -> recipe.getChildRecipes().size());
//        return recipes;
    }

    /**
     * 캐시 관리 메서드들
     */
    public void clearIdfCache() {
        idfCache.clear();
        lastIdfUpdateTime = 0;
        log.info("IDF 캐시가 초기화되었습니다.");
    }

    public void clearAllCaches() {
        clearIdfCache();
        allRecipesCache = null;
        lastRecipesUpdateTime = 0;
        log.info("모든 캐시가 초기화되었습니다.");
    }

    /**
     * 유사도 계산 모드 설정
     */
    public void setUseEuclideanDistance(boolean useEuclideanDistance) {
        this.useEuclideanDistance = useEuclideanDistance;
        log.info("유사도 계산 모드가 변경되었습니다: {}",
                useEuclideanDistance ? "유클리디안 거리" : "코사인 유사도");
    }

    public String getSimilarityMode() {
        return useEuclideanDistance ? "유클리디안 거리" : "코사인 유사도";
    }
}

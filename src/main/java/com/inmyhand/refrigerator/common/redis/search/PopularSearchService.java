package com.inmyhand.refrigerator.common.redis.search;

import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularSearchService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RecipeInfoRepository recipeInfoRepository;
    private final RedisKeyManager redisKeyManager;

    private  String POPULAR_SEARCH_KEY;
    private static final int POPULAR_SEARCH_SIZE = 5;
    private static final int EXPIRATION_HOURS = 120;

    @PostConstruct
    private void init() {
        POPULAR_SEARCH_KEY = redisKeyManager.getDailyPopularRecipeKey();
    }

    /**
     * 검색어 등록 및 조회수 증가
     */
    public void registerSearchKeyword(String keyword) {
        // 키워드가 비어있으면 처리하지 않음
        if (keyword == null || keyword.trim().length() <= 1) {
            return;
        }

        List<RecipeInfoEntity> list = recipeInfoRepository.findByRecipeNameContaining(keyword);

        if (list != null && !list.isEmpty()) {
            list.stream()
                    .map(RecipeInfoEntity::getRecipeName)
                    .forEach(recipeName -> {
                        redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, recipeName, 1);
                    });

        }


        // 24시간 유효기간 설정
        redisTemplate.expire(POPULAR_SEARCH_KEY, EXPIRATION_HOURS, TimeUnit.HOURS);

        log.info("검색어 [{}] 등록 완료", keyword);
    }

    /**
     * 상위 N개 인기 검색어 조회
     */
    public List<PopularSearchDto> getPopularSearches() {
        List<PopularSearchDto> popularSearches = new ArrayList<>();

        // Redis에서 인기 검색어 상위 10개 조회 (내림차순 정렬)
        Set<ZSetOperations.TypedTuple<Object>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(POPULAR_SEARCH_KEY, 0, POPULAR_SEARCH_SIZE - 1);

        if (typedTuples == null) {
            return popularSearches;
        }

        // 각 검색어에 대한 정보 조합
        typedTuples.forEach(tuple -> {
            String keyword = (String) tuple.getValue();
            Double score = tuple.getScore();

            // 해당 키워드와 일치하는 첫 번째 레시피만 찾기
            Optional<RecipeInfoEntity> recipeInfo = recipeInfoRepository.findFirstByRecipeName(keyword);

//             또는 리스트로 받아서 첫 번째 항목 사용
//             List<RecipeInfoEntity> recipeInfoList = recipeInfoRepository.findByRecipeName(keyword);
//             Optional<RecipeInfoEntity> recipeInfo = recipeInfoList.isEmpty() ?
//                                                  Optional.empty() : Optional.of(recipeInfoList.get(0));

            PopularSearchDto dto = PopularSearchDto.builder()
                    .keyword(keyword)
                    .count(score != null ? score.longValue() : 0)
                    .recipeId(recipeInfo.map(RecipeInfoEntity::getId).orElse(null))
                    .build();

            popularSearches.add(dto);
        });

        return popularSearches;
    }


    /**
     * 특정 날짜의 인기 검색어 조회
     */
    public List<PopularSearchDto> getPopularSearchesByDate(LocalDateTime dateTime) {
        String key = POPULAR_SEARCH_KEY + ":" + dateTime.format(DateTimeFormatter.ISO_DATE);
        List<PopularSearchDto> popularSearches = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, POPULAR_SEARCH_SIZE - 1);

        if (typedTuples == null) {
            return popularSearches;
        }

        typedTuples.forEach(tuple -> {
            String keyword = (String) tuple.getValue();
            Double score = tuple.getScore();

            Optional<RecipeInfoEntity> recipeInfo = recipeInfoRepository.findByRecipeName(keyword);

            PopularSearchDto dto = PopularSearchDto.builder()
                    .keyword(keyword)
                    .count(score != null ? score.longValue() : 0)
                    .recipeId(recipeInfo.map(RecipeInfoEntity::getId).orElse(null))
                    .build();

            popularSearches.add(dto);
        });

        return popularSearches;
    }

    /**
     * 인기 검색어 초기화
     */
    public void clearPopularSearches() {
        redisTemplate.delete(POPULAR_SEARCH_KEY);
    }
}

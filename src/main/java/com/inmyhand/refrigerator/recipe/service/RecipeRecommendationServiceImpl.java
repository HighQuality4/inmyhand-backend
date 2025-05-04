// RecipeRecommendationService.java
package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import com.inmyhand.refrigerator.fridge.repository.FridgeFoodRepository;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HateFoodEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.MemberAllergyEntity;
import com.inmyhand.refrigerator.healthinfo.repository.AllergyRepository;
import com.inmyhand.refrigerator.healthinfo.repository.HateFoodRepository;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRecommendationDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
import com.inmyhand.refrigerator.recipe.mapper.RecipeSummaryMapper;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationServiceImpl {

    private final RecipeInfoRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final FridgeFoodRepository fridgeFoodRepository;
    private final HateFoodRepository hateFoodRepository;
    private final AllergyRepository memberAllergyRepository;
    private final RecipeSummaryMapper recipeSummaryMapper;

    @Transactional(readOnly = true)
    public List<RecipeRecommendationDTO> recommendRecipes(Long memberId) {
        // 1. 사용자가 보유한 재료 목록 조회
        List<FridgeFoodEntity> userFoods = fridgeFoodRepository.findAllByMemberId(memberId);
        Set<String> availableIngredients = userFoods.stream()
                .map(FridgeFoodEntity::getFoodName)
                .collect(Collectors.toSet());

        // 2. 사용자가 싫어하는 음식 목록 조회
        List<HateFoodEntity> hateFoods = hateFoodRepository.findByMemberEntityId(memberId);
        Set<String> hatedIngredients = hateFoods.stream()
                .map(HateFoodEntity::getHateFood)
                .collect(Collectors.toSet());

        // 3. 사용자의 알레르기 목록 조회
        List<MemberAllergyEntity> allergies = memberAllergyRepository.findByMemberEntityId(memberId);
        Set<String> allergyIngredients = allergies.stream()
                .map(MemberAllergyEntity::getAllergy)
                .collect(Collectors.toSet());

        // 4. 제외할 재료 목록 생성 (싫어하는 음식 + 알레르기)
        Set<String> excludedIngredients = new HashSet<>();
        excludedIngredients.addAll(hatedIngredients);
        excludedIngredients.addAll(allergyIngredients);

        // 5. 레시피 추천 쿼리 실행
        List<RecipeInfoEntity> candidateRecipes = recipeRepository
                .findRecipesByAvailableIngredientsExcludingList(availableIngredients, excludedIngredients);

        // 6. 레시피 점수 계산 및 정렬
        List<RecipeRecommendationDTO> recommendations = new ArrayList<>();

        for (RecipeInfoEntity recipe : candidateRecipes) {
            List<RecipeIngredientEntity> recipeIngredients =
                    recipeIngredientRepository.getIngredientsByRecipeId(recipe.getId());

            // 레시피에 필요한 총 재료 수
            int totalIngredients = recipeIngredients.size();

            // 사용자가 보유한 재료 수
            int matchedIngredients = 0;

            // 레시피 재료가 제외 목록에 있는지 확인
            boolean hasExcludedIngredient = false;

            for (RecipeIngredientEntity ingredient : recipeIngredients) {
                String ingredientName = ingredient.getIngredientName();

                if (excludedIngredients.contains(ingredientName)) {
                    hasExcludedIngredient = true;
                    break;
                }

                if (availableIngredients.contains(ingredientName)) {
                    matchedIngredients++;
                }
            }

            // 제외 재료가 포함된 레시피는 제외
            if (hasExcludedIngredient) {
                continue;
            }

            // 재료 일치율 계산 (0.0 ~ 1.0)
            double matchRatio = (double) matchedIngredients / totalIngredients;

            RecipeRecommendationDTO dto = RecipeRecommendationDTO.builder()
                    .recipeId(recipe.getId())
                    .recipeName(recipe.getRecipeName())
                    .matchRatio(matchRatio)
                    .matchedIngredients(matchedIngredients)
                    .totalIngredients(totalIngredients)
                    .missingIngredients(totalIngredients - matchedIngredients)
                    .build();

            recommendations.add(dto);
        }

        // 재료 일치율이 높은 순으로 정렬
        recommendations.sort(Comparator.comparing(RecipeRecommendationDTO::getMatchRatio).reversed());

        // 상위 10개 추출
        List<RecipeRecommendationDTO> topRecommendations = recommendations.stream()
                .limit(10)
                .toList();
        recommendations = new ArrayList<>(topRecommendations);

        // 7. 결과 보정 로직
        int initialSize = recommendations.size();
        if (initialSize < 10) {
            int remaining = 10 - initialSize;
            List<Long> excludeIds = recommendations.stream()
                    .map(RecipeRecommendationDTO::getRecipeId)
                    .toList();


            List<RecipeInfoEntity> randomRecipes;
            if (excludeIds.isEmpty()) {
                // 초기 추천이 없는 경우 랜덤 10개
                randomRecipes = recipeRepository.findRandomRecipesExcludingIngredients(excludedIngredients, 10);
            } else {
                // 추가 추천이 필요한 경우
                randomRecipes = recipeRepository.findRandomRecipesExcludingIngredientsAndIds(
                        excludeIds, excludedIngredients, remaining);
            }

            for (RecipeInfoEntity recipe : randomRecipes) {
                if (recommendations.size() >= 10) break;

                boolean exists = recommendations.stream()
                        .anyMatch(dto -> dto.getRecipeId().equals(recipe.getId()));
                if (!exists) {
                    List<RecipeIngredientEntity> ingredients =
                            recipeIngredientRepository.getIngredientsByRecipeId(recipe.getId());

                    int total = ingredients.size();
                    int matched = (int) ingredients.stream()
                            .map(RecipeIngredientEntity::getIngredientName)
                            .filter(availableIngredients::contains)
                            .count();

                    RecipeRecommendationDTO dto = buildRecommendationDTO(recipe, total, matched);
                    recommendations.add(dto);
                }
            }
        }

        // 최종 10개 유지
        return recommendations.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    private RecipeRecommendationDTO buildRecommendationDTO(RecipeInfoEntity recipe, int total, int matched) {
        return RecipeRecommendationDTO.builder()
                .recipeId(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .matchRatio(total == 0 ? 0 : (double) matched / total)
                .matchedIngredients(matched)
                .totalIngredients(total)
                .missingIngredients(total - matched)
                .build();
    }


    @Transactional(readOnly = true)
    public List<RecipeSummaryDTO> recommendRecipes2(Long memberId) {
        // 1. 사용자 재료 조회
        Set<String> availableIngredients = getAvailableIngredients(memberId);

        // 2. 제외 재료 목록 생성
        Set<String> excludedIngredients = getExcludedIngredients(memberId);

        // 3. 후보 레시피 조회
        List<RecipeInfoEntity> candidateRecipes = getCandidateRecipes(availableIngredients, excludedIngredients);

        // 4. 필터링 및 정렬
        List<RecipeInfoEntity> filteredRecipes = filterAndSortRecipes(candidateRecipes, availableIngredients, excludedIngredients);

        // 5. 결과 매핑 및 보정
        return processRecommendations(filteredRecipes, excludedIngredients, availableIngredients);
    }

    private Set<String> getAvailableIngredients(Long memberId) {
        return fridgeFoodRepository.findAllByMemberId(memberId).stream()
                .map(FridgeFoodEntity::getFoodName)
                .collect(Collectors.toSet());
    }

    private Set<String> getExcludedIngredients(Long memberId) {
        Set<String> excluded = new HashSet<>();
        excluded.addAll(getHatedIngredients(memberId));
        excluded.addAll(getAllergyIngredients(memberId));
        return excluded;
    }

    private Set<String> getHatedIngredients(Long memberId) {
        return hateFoodRepository.findByMemberEntityId(memberId).stream()
                .map(HateFoodEntity::getHateFood)
                .collect(Collectors.toSet());
    }

    private Set<String> getAllergyIngredients(Long memberId) {
        return memberAllergyRepository.findByMemberEntityId(memberId).stream()
                .map(MemberAllergyEntity::getAllergy)
                .collect(Collectors.toSet());
    }

    private List<RecipeInfoEntity> getCandidateRecipes(Set<String> available, Set<String> excluded) {
        return recipeRepository.findRecipesByAvailableIngredientsExcludingList(available, excluded);
    }

    private List<RecipeInfoEntity> filterAndSortRecipes(List<RecipeInfoEntity> candidates,
                                                        Set<String> available,
                                                        Set<String> excluded) {
        return candidates.stream()
                .filter(recipe -> isValidRecipe(recipe, excluded))
                .sorted(Comparator.comparingDouble(recipe ->
                        -calculateMatchRatio(recipe, available)))
                .collect(Collectors.toList());
    }

    private boolean isValidRecipe(RecipeInfoEntity recipe, Set<String> excluded) {
        return recipe.getRecipeIngredientList().stream()
                .noneMatch(ri -> excluded.contains(ri.getIngredientName()));
    }

    private double calculateMatchRatio(RecipeInfoEntity recipe, Set<String> available) {
        long matched = recipe.getRecipeIngredientList().stream()
                .filter(ri -> available.contains(ri.getIngredientName()))
                .count();
        return (double) matched / recipe.getRecipeIngredientList().size();
    }

    private List<RecipeSummaryDTO> processRecommendations(List<RecipeInfoEntity> filtered,
                                                          Set<String> excluded,
                                                          Set<String> available) {
        List<RecipeInfoEntity> result = new ArrayList<>(filtered.stream()
                .limit(10)
                .collect(Collectors.toList()));

        if (result.size() < 10) {
            result.addAll(getRandomRecipes(excluded, 10 - result.size(), result));
        }

        initializeAssociations(result);
        return recipeSummaryMapper.toDtoList(result.stream().limit(10).collect(Collectors.toList()));
    }

    private List<RecipeInfoEntity> getRandomRecipes(Set<String> excluded, int limit, List<RecipeInfoEntity> existing) {
        List<Long> excludeIds = existing.stream()
                .map(RecipeInfoEntity::getId)
                .collect(Collectors.toList());

        return excludeIds.isEmpty() ?
                recipeRepository.findRandomRecipesExcludingIngredients(excluded, limit) :
                recipeRepository.findRandomRecipesExcludingIngredientsAndIds(excludeIds, excluded, limit);
    }

    private void initializeAssociations(List<RecipeInfoEntity> recipes) {
        recipes.forEach(recipe -> {
            Hibernate.initialize(recipe.getMemberEntity());
            Hibernate.initialize(recipe.getFilesEntities());
            Hibernate.initialize(recipe.getRecipeCategoryList());
        });
    }

}


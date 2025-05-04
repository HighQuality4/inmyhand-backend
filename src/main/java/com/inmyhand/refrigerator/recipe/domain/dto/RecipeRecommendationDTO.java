// RecipeRecommendationDTO.java
package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecipeRecommendationDTO {
    private Long recipeId;
    private String recipeName;
    private double matchRatio;       // 보유 재료와 레시피 재료의 일치율 (0.0 ~ 1.0)
    private int matchedIngredients;  // 일치하는 재료 수
    private int totalIngredients;    // 총 필요 재료 수
    private int missingIngredients;  // 부족한 재료 수
    private RecipeSummaryDTO recipeSummary; // 추가
}

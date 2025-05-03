package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;

public interface RecipeNutrientService {
    RecipeNutrientAnalysisEntityDto getNutritionAnalysis(Long recipeId, Long memberId);
}

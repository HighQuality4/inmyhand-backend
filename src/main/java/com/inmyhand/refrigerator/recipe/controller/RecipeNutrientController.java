package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;
import com.inmyhand.refrigerator.recipe.service.RecipeNutrientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/nutrient")
@RequiredArgsConstructor
public class RecipeNutrientController {
    private final RecipeNutrientService nutrientService;

    @PostMapping("/{recipeId}")
    public RecipeNutrientAnalysisEntityDto analyzeNutrition(
            @PathVariable Long recipeId,
            @RequestParam Long memberId) {
        return nutrientService.getNutritionAnalysis(recipeId, memberId);
    }
}

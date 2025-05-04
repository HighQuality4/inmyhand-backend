package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;
import com.inmyhand.refrigerator.recipe.service.RecipeNutrientService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/nutrient")
@RequiredArgsConstructor
public class RecipeNutrientController {
    private final RecipeNutrientService nutrientService;

    @PostMapping("/{recipeId}")
    public RecipeNutrientAnalysisEntityDto analyzeNutrition(
            @PathVariable("recipeId") Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return nutrientService.getNutritionAnalysis(recipeId, userDetails.getUserId());
    }
}

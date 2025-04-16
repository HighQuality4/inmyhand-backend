package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeNutrientAnalysisDTO {
    private String analysisResult;
    private int score;
    private int carbs;
    private int protein;
    private int fat;
    private int mineral;
    private int vitamin;
}

package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeNutrientAnalysisEntityDto implements Serializable {
    private Long id;
    private String analysisResult;
    private int fitnessScore;
    private int carbohydrate;
    private int protein;
    private int fat;
    private int mineral;
    private int vitamin;
}
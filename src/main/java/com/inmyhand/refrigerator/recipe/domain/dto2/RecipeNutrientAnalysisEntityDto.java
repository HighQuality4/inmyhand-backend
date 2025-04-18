package com.inmyhand.refrigerator.recipe.domain.dto2;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity}
 */
@Data
public class RecipeNutrientAnalysisEntityDto implements Serializable {
    private Long id;
    private String analysisResult;
    private int score;
    private int carbs;
    private int protein;
    private int fat;
    private int mineral;
    private int vitamin;
}
package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecipeRequestDTO {
    private Long userId;
    private Long parentRecipeId;

    @NotBlank
    private String recipeName;

    @NotNull
    private DifficultyEnum difficulty;

    @NotBlank
    private String cookingTime;

    private Integer calories;
    private String summary;
    private Integer servings;

    private String file;
    private List<RecipeIngredientEntityDto> ingredients;
    private List<RecipeStepsEntityDto> steps;
    private List<RecipeCategoryEntityDto> categories;
}

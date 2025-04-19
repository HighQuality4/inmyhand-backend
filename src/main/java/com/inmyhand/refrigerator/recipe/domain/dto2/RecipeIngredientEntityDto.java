package com.inmyhand.refrigerator.recipe.domain.dto2;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity}
 */
@Data
public class RecipeIngredientEntityDto implements Serializable {

    private Long id;

    @NotBlank
    private String ingredientName;

    private String ingredientGroup;

    @Min(0)
    private double ingredientQuantity;

    private String ingredientUnit;
}
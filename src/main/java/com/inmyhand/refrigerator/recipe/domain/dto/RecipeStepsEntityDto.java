package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeStepsEntityDto implements Serializable {
    private Long id;
    private Integer stepNumber;
    private String stepDescription;
    private String fileUrl;
}
package com.inmyhand.refrigerator.recipe.domain.dto2;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity}
 */
@Data
public class RecipeStepsEntityDto implements Serializable {
    private Long id;
    private Integer stepNumber;
    private String stepDescription;
    private String fileUrl;
}
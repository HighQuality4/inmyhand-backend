package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.CategoryTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeCategoryEntity}
 */
@Data
@AllArgsConstructor
public class RecipeCategoryEntityDto implements Serializable {

    private Long id;

    @NotBlank
    private String recipeCategoryName;

    @NotBlank
    private CategoryTypeEnum recipeCategoryType;


}
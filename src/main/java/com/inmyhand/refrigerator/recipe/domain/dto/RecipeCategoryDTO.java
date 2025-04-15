package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCategoryDTO {
    private String categoryName;
    private CategoryTypeEnum categoryType;
}
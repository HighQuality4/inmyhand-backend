package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepDTO {
    private Integer stepNumber;
    private String stepDescription;
    private String fileUrl;
}
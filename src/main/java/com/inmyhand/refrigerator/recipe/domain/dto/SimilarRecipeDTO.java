package com.inmyhand.refrigerator.recipe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimilarRecipeDTO {

    private Long id;
    private String recipeName;
    private String fileUrl;

}

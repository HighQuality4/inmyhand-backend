package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSummaryDTO {
    private List<String> fileUrl;
    private List<RecipeCategoryDTO> categories;
    private String recipeName;
    private DifficultyEnum difficulty;
    private String cookingTime;
    private Integer calories;
    private Long likeCount;
    private String nickname;
}
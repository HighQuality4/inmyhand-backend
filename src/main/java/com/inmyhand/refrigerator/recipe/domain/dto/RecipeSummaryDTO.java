package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import lombok.*;

import java.util.List;

@Data
public class RecipeSummaryDTO {
    private Long id;
    private List<String> fileUrl;
    private List<RecipeCategoryEntityDto> categories;
    private String recipeName;
    private DifficultyEnum difficulty;
    private String cookingTime;
    private Integer calories;
    private Long likeCount;
    private String nickname;
}
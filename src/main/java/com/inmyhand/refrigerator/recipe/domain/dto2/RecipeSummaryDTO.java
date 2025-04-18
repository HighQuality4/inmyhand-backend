package com.inmyhand.refrigerator.recipe.domain.dto2;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCategoryDTO;
import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import lombok.*;

import java.util.List;

@Data
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
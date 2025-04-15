package com.inmyhand.refrigerator.recipe.domain.dto;

import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetailDTO {
    private Long parentRecipeId;
    private String recipeName;
    private List<RecipeCategoryDTO> categories;
    private List<String> fileUrl;
    private Integer servings;
    private DifficultyEnum difficulty;
    private String cookingTime;
    private Integer calories;
    private String nickname;
    private Long likeCount;
    private Long viewCount;
    private String summary;
    private Date createdAt;
    private Date updatedAt;
    private List<RecipeIngredientDTO> ingredients;
    private List<RecipeStepDTO> steps;
    private RecipeNutrientAnalysisDTO analysis;
    private List<RecipeCommentDTO> comments;
}
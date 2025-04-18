package com.inmyhand.refrigerator.recipe.domain.dto2;

import com.inmyhand.refrigerator.recipe.domain.dto.*;
import com.inmyhand.refrigerator.recipe.domain.enums.CookingTimeEnum;
import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity}
 */
@Data
public class RecipeDetailDTO implements Serializable {
    private Long id;

    private String recipeName;
    private DifficultyEnum difficulty;
    private CookingTimeEnum cookingTime;
    private Integer calories;
    private String summary;
    private Integer servings;
    private Integer recipeDepth;
    private Date createdAt;
    private Date updatedAt;

    private String nickname;
    private Long likeCount;
    private Long viewCount;

    private List<RecipeCategoryEntityDto> categories;
    private List<String> fileUrl;
    private List<RecipeIngredientEntityDto> ingredients;
    private List<RecipeStepsEntityDto> steps;
    private List<RecipeCommentEntityDto> comments;
    private RecipeNutrientAnalysisDTO analysis;

}
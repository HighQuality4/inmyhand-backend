package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto2.RecipeSummaryDTO;

import java.util.List;

// 조인 기반 상세 조회, 리스트 조회 전용
public interface RecipeQueryService {
    public List<RecipeSummaryDTO> getAllRecipeList();

    public List<RecipeSummaryDTO> getPopularRecipeList();

    public List<RecipeSummaryDTO> getArrayRecipeList(String orderBy, String sortType);

    public RecipeDetailDTO getRecipeDetail(Long recipeId);
}

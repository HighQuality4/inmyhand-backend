package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 조인 기반 상세 조회, 리스트 조회 전용
public interface RecipeQueryService {
    public Page<RecipeSummaryDTO> getAllRecipeList(Pageable pageable);

    public List<RecipeSummaryDTO> getPopularRecipeList();

    public Page<RecipeSummaryDTO> getArrayRecipeList(String orderBy, String sortType, int page, int size);

    public RecipeDetailDTO getRecipeDetail(Long recipeId);

    public Page<RecipeSummaryDTO> getSearchRecipeList(String keyword, int page, int size);

    public List<RecipeSummaryDTO> getMyRecipeList(Long userId);

    public List<RecipeSummaryDTO> getMyLikeRecipeList(Long userId);
}

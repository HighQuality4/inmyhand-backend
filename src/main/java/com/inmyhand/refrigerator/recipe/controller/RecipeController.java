package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeInteractionService;
import com.inmyhand.refrigerator.recipe.service.RecipeNutrientService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeQueryService recipeQueryService;
    private RecipeNutrientService recipeNutrientService;
    private RecipeInteractionService recipeInteractionService;

    // 전체 레시피 목록 조회
    @GetMapping
    public List<RecipeSummaryDTO> getAllRecipeList() {
        return recipeQueryService.getAllRecipeList();
    }

    // 인기 레시피 목록 조회
    @GetMapping("/popular")
    public List<RecipeSummaryDTO> getPopularRecipeList() {
        return recipeQueryService.getPopularRecipeList();
    }

    // 레시피 목록 정렬 조회
    @GetMapping("/sort/")
    public List<RecipeSummaryDTO> getArrayRecipeList(@RequestParam(name = "sortBy", required = false) String sortBy,
                                                  @RequestParam(name = "type", required = false, defaultValue = "ASC") String type) {
        return recipeQueryService.getArrayRecipeList(sortBy, type);
    }

    // 레시피 상세 조회
    @GetMapping("/{id}")
    public RecipeDetailDTO getRecipeDetail(@PathVariable("id") Long recipeId) {
        return recipeQueryService.getRecipeDetail(recipeId);
    }

    // TODO : 레시피 검색 /search

    // TODO : 레시피 생성 /api/recipes/create?

    // TODO : 레시피 수정 PUT /api/recipes/{id}

    // TODO : 레시피 삭제 DELETE /api/recipes/{id}

}
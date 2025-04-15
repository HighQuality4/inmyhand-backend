package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.*;
import com.inmyhand.refrigerator.recipe.service.RecipeInteractionService;
import com.inmyhand.refrigerator.recipe.service.RecipeNutrientService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/home")
public class RecipeHomeController {
    @Autowired
    private RecipeQueryService recipeQueryService;
    private RecipeNutrientService recipeNutrientService;
    private RecipeInteractionService recipeInteractionService;

    // 모든 레시피 목록 조회
    @GetMapping("/flist")
    public List<RecipeSummaryDTO> getAllRecipeList() {
        return recipeQueryService.getAllRecipeList();
    }

    // 인기레시피 목록 조회
    @GetMapping("/plist")
    public List<RecipeSummaryDTO> getPopularRecipeList() {
        return recipeQueryService.getPopularRecipeList();
    }

    @GetMapping("/array/")
    public List<RecipeSummaryDTO> getArrayRecipeList(@RequestParam(name = "orderBy", required = false) String orderBy,
                                                  @RequestParam(name = "sortType", required = false, defaultValue = "ASC") String sortType) {
        return recipeQueryService.getArrayRecipeList(orderBy, sortType);
    }

    @GetMapping("/{id}")
    public RecipeDetailDTO getRecipeDetail(@PathVariable("id") Long recipeId) {
        return recipeQueryService.getRecipeDetail(recipeId);
    }

}
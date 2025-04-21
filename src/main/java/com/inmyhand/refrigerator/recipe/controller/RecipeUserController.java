package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes/my")
public class RecipeUserController {
    @Autowired
    private RecipeQueryService recipeQueryService;

    // TODO : 내가 등록한 레시피 조회 /api/recipes/my
    @GetMapping
    public List<RecipeSummaryDTO> getMyRecipeList(@AuthenticationPrincipal MemberEntityDto user) {
        Long userId = user.getId();
        return recipeQueryService.getMyRecipeList(userId);
    }

    // TODO : 내가 좋아요한 레시피 조회 /api/recipes/my/likes
    @GetMapping("/likes")
    public List<RecipeSummaryDTO> getMyLikeRecipeList(@AuthenticationPrincipal MemberEntityDto user) {
        Long userId = user.getId();
        return recipeQueryService.getMyLikeRecipeList(userId);
    }
}

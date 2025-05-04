package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRecommendationDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeRecommendationServiceImpl;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes/recommend")
@RequiredArgsConstructor
public class RecipeRecommendController {

    private final RecipeRecommendationServiceImpl recipeRecommendationService;

    // 유사도와 리스트 같이 나오게 수정, 1에 가까울수록 정확함
    @PostMapping
    public List<RecipeRecommendationDTO> getRecommendations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        // TODO 유저 아이디가 없다면 처리
        List<RecipeRecommendationDTO> recommendations = recipeRecommendationService.recommendRecipes(userId);
        return recommendations;
    }

    // 유사도 안나옴, Summary DTO 가 나옴
    @PostMapping("/list")
    public List<RecipeSummaryDTO> getRecommendations2(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Long userId = customUserDetails.getUserId();
        // TODO 유저 아이디가 없다면 처리
        List<RecipeSummaryDTO> recipeSummaryDTO = recipeRecommendationService.recommendRecipes2(userId);
        return recipeSummaryDTO;
    }

}

package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRecommendationDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeRecommendationServiceImpl;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecipeRecommendController {

    private final RecipeRecommendationServiceImpl recipeRecommendationService;

    // 유사도 나옴, 1에 가까울수록 정확함
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> getRecommendations(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        // TODO 유저 아이디가 없다면 처리
//        List<RecipeRecommendationDTO> recommendations = recipeRecommendationService.recommendRecipes(userId);
        List<RecipeRecommendationDTO> recommendations = recipeRecommendationService.recommendRecipes(2L);
        return ResponseEntity.ok(Map.of("recommend", recommendations));
    }


    // 유사도 안나옴, Summary DTO 가 나옴
    @GetMapping("/test2")
    public ResponseEntity<Map<String, Object>> getRecommendations2(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        // TODO 유저 아이디가 없다면 처리
//        List<RecipeSummaryDTO> recipeSummaryDTOS = recipeRecommendationService.recommendRecipes2(userId);
        List<RecipeSummaryDTO> recipeSummaryDTOS = recipeRecommendationService.recommendRecipes2(2L);
        return ResponseEntity.ok(Map.of("recommend", recipeSummaryDTOS));
    }

}

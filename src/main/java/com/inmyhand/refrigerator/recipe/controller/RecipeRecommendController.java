package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRecommendationDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeRecommendationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecipeRecommendController {

    private final RecipeRecommendationServiceImpl recipeRecommendationService;

    @GetMapping("/test/{memberId}")
    public ResponseEntity<Map<String, Object>> getRecommendations(@PathVariable("memberId") Long memberId) {
        List<RecipeRecommendationDTO> recommendations = recipeRecommendationService.recommendRecipes(memberId);
        return ResponseEntity.ok(Map.of("recommend", recommendations));
    }

    @GetMapping("/test2/{memberId}")
    public ResponseEntity<Map<String, Object>> getRecommendations2(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(Map.of("recommend", recipeRecommendationService.recommendRecipes2(memberId)));
    }

}

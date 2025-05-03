package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.service.RecipeViewService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/views")
@RequiredArgsConstructor
public class RecipeViewController {

    private final RecipeViewService recipeViewService;

    // 조회수 증가 id
    // 들어오는 형태 : localhost:7079/api/recipe/views/54
    @PostMapping("/{recipeId}")
    public ResponseEntity<?> addRecipeView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable("recipeId") Long recipeId) {
        if(userDetails != null && userDetails.getUserId() != null) {
            recipeViewService.addRecipeView(userDetails.getUserId(), recipeId);
        }
        return ResponseEntity.ok().build();
    }
}

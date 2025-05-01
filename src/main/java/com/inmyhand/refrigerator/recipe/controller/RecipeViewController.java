package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.service.RecipeViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes/views")
@RequiredArgsConstructor
public class RecipeViewController {

    private final RecipeViewService recipeViewService;

    // 조회수 증가 id
    // 들어오는 형태 : localhost:7079/api/recipe/views/54?memberId=2

    @GetMapping("/{recipeId}")
    public ResponseEntity<?> addRecipeView(@RequestParam("memberId") Long memberId, @PathVariable("recipeId") Long recipeId) {
        recipeViewService.addRecipeView(memberId, recipeId);
        return ResponseEntity.ok().build();
    }
}

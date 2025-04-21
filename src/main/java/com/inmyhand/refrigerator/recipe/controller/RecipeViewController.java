package com.inmyhand.refrigerator.recipe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes/views")
public class RecipeViewController {
    // 조회수 증가 /api/recipes/views/{recipeId}
}

package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.service.RecipeLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes/likes")
@RequiredArgsConstructor
public class RecipeLikeController {

    private final RecipeLikeService recipeLikeService;

    // localhost:7079/api/recipes/likes/36?memberId=2

    @PostMapping("/{recipeId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long recipeId,
            @RequestAttribute("memberId") Long memberId) {

        boolean isLiked = recipeLikeService.toggleRecipeLike(memberId, recipeId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("message", isLiked ? "레시피를 좋아요 했습니다." : "레시피 좋아요를 취소했습니다.");

        return ResponseEntity.ok(Map.of("message", response));
    }
}

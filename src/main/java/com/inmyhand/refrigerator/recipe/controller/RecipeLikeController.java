package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.service.RecipeLikeService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes/likes")
@RequiredArgsConstructor
public class RecipeLikeController {

    private final RecipeLikeService recipeLikeService;

    // 좋아요 입력
    // localhost:7079/api/recipes/likes/36
    @PostMapping("/{recipeId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable("recipeId") Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isLiked = recipeLikeService.toggleRecipeLike(userDetails.getUserId(), recipeId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("message", isLiked ? "좋아요한 레시피에 담겼습니다." : "좋아요를 취소했습니다");

        return ResponseEntity.ok(Map.of("message", response));
    }

    // 로그인 사용자 - 좋아요 조회
    // localhost:7079/api/recipes/likes/check/36
    @PostMapping("/check/{recipeId}")
    public ResponseEntity<Boolean> checkLikeStatus(
            @PathVariable("recipeId") Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        boolean isLiked = recipeLikeService.isRecipeLikedByMember(userDetails.getUserId(), recipeId);
        return ResponseEntity.ok(isLiked);
    }
}

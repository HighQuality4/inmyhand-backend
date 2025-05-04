package com.inmyhand.refrigerator.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCommentEntityDto;
import com.inmyhand.refrigerator.recipe.service.RecipeCommentService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes/comments")
@RequiredArgsConstructor
public class RecipeCommentController {
    private final RecipeCommentService recipeCommentService;
    private final ObjectMapper objectMapper;

    // 댓글 등록
    @PostMapping("/{recipeId}")
    public RecipeCommentEntityDto addComment(
            @PathVariable("recipeId") Long recipeId,
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> innerParam = (Map<String, Object>) body.get("param");
        List<Map<String, Object>> paramList = (List<Map<String, Object>>) innerParam.get("param");
        Map<String, Object> commentData = paramList.get(0);
        RecipeCommentEntityDto commentDto = objectMapper.convertValue(commentData, RecipeCommentEntityDto.class);

        RecipeCommentEntityDto response = recipeCommentService.addComment(
                commentDto.getCommentContents(), userDetails.getUserId(), recipeId
        );

        return response;
    }

    /**
     * 댓글 삭제 API
     * @param commentId 댓글 ID
     * @return 응답 메시지
     */
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable("commentId") Long commentId) {

        recipeCommentService.deleteComment(commentId);

        return ResponseEntity.ok(Map.of("message", "댓글이 삭제되었습니다."));
    }
}

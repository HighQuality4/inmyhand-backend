package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCommentEntityDto;
import com.inmyhand.refrigerator.recipe.service.RecipeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes/comments")
@RequiredArgsConstructor
public class RecipeCommentController {
    // TODO : 댓글 등록 /api/recipes/comments/{recipeId}

    // TODO : 댓글 삭제 /api/recipes/comments/delete/{commentId}

    private final RecipeCommentService recipeCommentService;

    /**
     * 댓글 등록 API
     * @param recipeId 레시피 ID
     * @param commentDto 댓글 정보 DTO
     * @param memberId 회원 ID (인증정보에서 추출)
     * @return 응답 메시지
     */
    @PostMapping("/{recipeId}")
    public ResponseEntity<Map<String, String>> addComment(
            @PathVariable Long recipeId,
            @RequestBody RecipeCommentEntityDto commentDto,
            @RequestAttribute("memberId") Long memberId) {

        recipeCommentService.addComment(commentDto.getCommentContents(), memberId, recipeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "댓글이 성공적으로 등록되었습니다."));
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

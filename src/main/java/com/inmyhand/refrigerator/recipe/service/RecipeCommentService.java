package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCommentEntityDto;

public interface RecipeCommentService {
    RecipeCommentEntityDto addComment(String commentContents, Long memberId, Long recipeId);
    void deleteComment(Long commentId);
}

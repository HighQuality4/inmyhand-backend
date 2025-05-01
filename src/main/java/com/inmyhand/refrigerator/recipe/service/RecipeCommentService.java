package com.inmyhand.refrigerator.recipe.service;

public interface RecipeCommentService {
    void addComment(String commentContents, Long memberId, Long recipeId);
    void deleteComment(Long commentId);
}

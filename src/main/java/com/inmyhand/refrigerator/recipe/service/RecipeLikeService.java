package com.inmyhand.refrigerator.recipe.service;

public interface RecipeLikeService {
    boolean toggleRecipeLike(Long memberId, Long recipeId);
    boolean isRecipeLikedByMember(Long memberId, Long recipeId);
}

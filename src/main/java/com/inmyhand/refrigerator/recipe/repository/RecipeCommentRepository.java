package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCommentRepository extends JpaRepository<RecipeCommentEntity, Long> {
    // 댓글 생성

    // 댓글 삭제
}

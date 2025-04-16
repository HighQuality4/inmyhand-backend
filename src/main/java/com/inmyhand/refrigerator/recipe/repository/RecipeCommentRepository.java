package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecipeCommentRepository extends JpaRepository<RecipeCommentEntity, Long> {
    // 댓글 생성

    @Query("select c from RecipeCommentEntity c " +
            "left join fetch c.memberEntity " +
            "left join fetch c.recipeInfoEntity " +
            "where c.id=:id")
    Optional<RecipeCommentEntity> findByIdCustom(@Param("id") Long id);
    // 댓글 삭제
}

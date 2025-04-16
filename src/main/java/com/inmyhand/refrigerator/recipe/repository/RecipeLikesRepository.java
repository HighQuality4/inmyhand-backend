package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeLikesRepository extends JpaRepository<RecipeLikesEntity, Long> {
    // 좋아요 생성

    // 좋아요 삭제
}

package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeViewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeViewsRepository extends JpaRepository<RecipeViewsEntity, Long> {
    // 조회수 생성
}

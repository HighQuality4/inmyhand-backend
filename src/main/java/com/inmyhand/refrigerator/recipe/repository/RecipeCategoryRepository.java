package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategoryEntity, Long> {
}

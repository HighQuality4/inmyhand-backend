package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStepsEntity, Long> {

}

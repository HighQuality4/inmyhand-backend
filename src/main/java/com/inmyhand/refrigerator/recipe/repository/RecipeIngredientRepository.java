package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientEntity, Long> {
    // 레시피 재료 등록

    // 레시피 재료 수정

    @Query("SELECT ri FROM RecipeIngredientEntity ri WHERE ri.recipeInfoEntity.id = :recipeId")
    List<RecipeIngredientEntity> getIngredientsByRecipeId(@Param("recipeId") Long recipeId);
}

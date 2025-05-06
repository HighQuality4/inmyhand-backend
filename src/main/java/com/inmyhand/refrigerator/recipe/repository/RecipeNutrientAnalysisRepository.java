package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeNutrientAnalysisRepository extends JpaRepository<RecipeNutrientAnalysisEntity, Long> {
    // 이전에 동일한 요청이 있었는지 확인
    Optional<RecipeNutrientAnalysisEntity> findByRecipeInfoEntity_IdAndMemberEntity_Id(Long recipeId, Long memberId);
}

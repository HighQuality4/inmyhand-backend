package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeNutrientAnalysisRepository extends JpaRepository<RecipeNutrientAnalysisEntity, Long> {
    // AI 돌리기 전 이전에 동일한 레시피와 관심건강정보로 영양소분석한 내역있는지 조회 - 수정 필요
    @Query(value = "SELECT EXISTS (SELECT 1 FROM recipe_nutrient_analysis rna JOIN analysis_health_interest ahi \n" +
            "ON rna.nutrient_analysis_id = ahi.nutrient_analysis_id WHERE rna.recipe_id = :recipeId \n" +
            "AND ahi.health_interest_category_id IN (:categoryIds) GROUP BY rna.nutrient_analysis_id \n" +
            "HAVING COUNT(DISTINCT ahi.health_interest_category_id) = :size)", nativeQuery = true)
    Boolean checkDuplicateAnalysis(@Param("recipeId") Long recipeId, @Param("categoryIds") List<Long> categoryIds, @Param("size") int size);

    // 1개의 레시피의 영양소분석 내용 조회
}

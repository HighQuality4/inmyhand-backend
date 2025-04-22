package com.inmyhand.refrigerator.category.repository;

import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodVectorRepository  {
    Optional<FoodVectorRequestDTO> findMostSimilarCategoryByVector(String inputText, String vector);

    void insertFoodVector(String categoryName, String naturalText, int expirationInfo, String embeddingStr);
}

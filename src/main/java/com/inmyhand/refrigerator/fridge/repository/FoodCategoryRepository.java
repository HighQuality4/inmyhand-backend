package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.FoodCategoryEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Long> {
    FoodCategoryEntity findByCategoryName(String categoryName);
}

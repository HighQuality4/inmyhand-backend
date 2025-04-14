package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FridgeFoodRepository extends JpaRepository<FridgeFoodEntity, Long> {
    List<FridgeFoodEntity> findByFridgeEntityId(Long fridgeId);
}

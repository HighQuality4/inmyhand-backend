package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeRepository extends JpaRepository<FridgeEntity, Long> {
}

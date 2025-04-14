package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FoodCategoryEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import com.inmyhand.refrigerator.fridge.repository.FoodCategoryRepository;
import com.inmyhand.refrigerator.fridge.repository.FridgeFoodRepository;
import com.inmyhand.refrigerator.fridge.repository.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeFoodService {

    private final FridgeFoodRepository fridgeFoodRepository;
    private final FridgeRepository fridgeRepository;
    private final FoodCategoryRepository foodCategoryRepository;

    // 1. Create
    public void svcCreateFridgeFood(FridgeFoodDTO dto) {
        FridgeEntity fridge = fridgeRepository.findById(dto.getFridgeId())
                .orElseThrow(() -> new IllegalArgumentException("냉장고 없음"));

        FoodCategoryEntity category = foodCategoryRepository.findById(dto.getFoodCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        FridgeFoodEntity food = FridgeFoodEntity.builder()
                .foodName(dto.getFoodName())
                .foodAmount(dto.getFoodAmount())
                .endDate(dto.getEndDate())
                .chargeDate(dto.getChargeDate())
                .saveDate(dto.getSaveDate())
                .fridgeEntity(fridge)
                .foodCategoryEntity(category)
                .build();

        fridgeFoodRepository.save(food);
    }

    // 2. Update
    public void svcUpdateFridgeFood(Long fridgeFoodId, FridgeFoodDTO dto) {
        FridgeFoodEntity existing = fridgeFoodRepository.findById(fridgeFoodId)
                .orElseThrow(() -> new IllegalArgumentException("식재료 없음"));

        FridgeEntity fridge = fridgeRepository.findById(dto.getFridgeId())
                .orElseThrow(() -> new IllegalArgumentException("냉장고 없음"));

        FoodCategoryEntity category = foodCategoryRepository.findById(dto.getFoodCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        FridgeFoodEntity updated = FridgeFoodEntity.builder()
                .id(existing.getId()) // ID 유지
                .foodName(dto.getFoodName())
                .foodAmount(dto.getFoodAmount())
                .endDate(dto.getEndDate())
                .chargeDate(dto.getChargeDate())
                .saveDate(dto.getSaveDate())
                .fridgeEntity(fridge)
                .foodCategoryEntity(category)
                .build();

        fridgeFoodRepository.save(updated);
    }

    // 3. Delete
    public void svcDeleteFridgeFood(Long fridgeFoodId) {
        if (!fridgeFoodRepository.existsById(fridgeFoodId)) {
            throw new IllegalArgumentException("식재료 없음");
        }
        fridgeFoodRepository.deleteById(fridgeFoodId);
    }

    // 4. 냉장고별 전체 리스트
    public List<FridgeFoodDTO> svcGetAllFridgeFoods(Long fridgeId) {
        return fridgeFoodRepository.findByFridgeEntityId(fridgeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 5. 단건 조회
    public Optional<FridgeFoodDTO> svcGetFridgeFoodById(Long fridgeFoodId) {
        return fridgeFoodRepository.findById(fridgeFoodId)
                .map(this::toDto);
    }

    // Entity → DTO 변환
    private FridgeFoodDTO toDto(FridgeFoodEntity entity) {
        return FridgeFoodDTO.builder()
                .id(entity.getId())
                .foodName(entity.getFoodName())
                .foodAmount(entity.getFoodAmount())
                .endDate(entity.getEndDate())
                .chargeDate(entity.getChargeDate())
                .saveDate(entity.getSaveDate())
                .fridgeId(entity.getFridgeEntity().getId())
                .foodCategoryId(entity.getFoodCategoryEntity().getId())
                .build();
    }
}

package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeMainPageDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeWithRolesDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FoodCategoryEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.repository.*;
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
    private final FridgeMemberRepository fridgeMemberRepository;
    private final MemberGroupRoleRepository memberGroupRoleRepository;

    // 냉장고 main 페이지 정보 출력
    public FridgeMainPageDTO svcGetFridgeMainPage(Long memberId) {
        // 1. 즐겨찾기 냉장고 리스트
        List<FridgeDTO> favoriteFridges = svcGetFavoriteFridgeDetail(memberId);

        // 2. 메인 냉장고 ID
        Long mainFridgeId = svcFindMainFridgeId(memberId);

        // 3. 메인 냉장고 식재료
        List<FridgeFoodDTO> foodList = svcGetAllFridgeFoods(mainFridgeId);

        return new FridgeMainPageDTO(mainFridgeId, favoriteFridges, foodList);
    }

    // 냉장고 변경시 (rest) 다른 냉장고 정보 출력


    // 내가 참여하고 있는 냉장고 리스트 정보 출력
    public List<FridgeWithRolesDTO> svcGetFridgeListWithRoles(Long memberId) {
        List<FridgeMemberEntity> fridgeMembers = fridgeMemberRepository.findByMemberEntity_Id(memberId);

        return fridgeMembers.stream().map(fridgeMember -> {
            List<String> roleNames = memberGroupRoleRepository.findAllByFridgeMemberEntity_Id(fridgeMember.getId())
                    .stream()
                    .map(memberGroupRole -> memberGroupRole.getGroupRoleEntity().getRoleName())
                    .collect(Collectors.toList());

            return FridgeWithRolesDTO.builder()
                    .fridgeId(fridgeMember.getFridgeEntity().getId())
                    .fridgeName(fridgeMember.getFridgeEntity().getFridgeName())
                    .roleNames(roleNames)
                    .build();
        }).collect(Collectors.toList());
    }


    public List<FridgeDTO> svcGetFavoriteFridgeDetail(Long memberId) {
        return fridgeMemberRepository.findByMemberEntity_IdAndFavoriteStateTrue(memberId)
                .stream()
                .map(fridgeMember -> convertToDto(fridgeMember.getFridgeEntity()))
                .collect(Collectors.toList());
    }

    public Long svcFindMainFridgeId(Long memberId) {
        return fridgeMemberRepository.findFirstByMemberEntity_IdAndFavoriteStateTrueOrderByJoinDateAsc(memberId)
                .map(fridgeMember -> fridgeMember.getFridgeEntity().getId())
                .orElseThrow(() -> new RuntimeException("메인 냉장고 없음"));
    }
    //  냉장고별 전체 리스트
    public List<FridgeFoodDTO> svcGetAllFridgeFoods(Long fridgeId) {
        return fridgeFoodRepository.findByFridgeEntityId(fridgeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //--------------------------DTO 변환-----------------------------
    // 냉장고 → DTO 변환
    private FridgeDTO convertToDto(FridgeEntity fridge) {
        return FridgeDTO.builder()
                .id(fridge.getId())
                .fridgeName(fridge.getFridgeName())
                .build();
    }

    // 식재료 → DTO 변환
    private FridgeFoodDTO convertToDto(FridgeFoodEntity food) {
        return FridgeFoodDTO.builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .foodAmount(food.getFoodAmount())
                .endDate(food.getEndDate())
                .chargeDate(food.getChargeDate())
                .saveDate(food.getSaveDate())
                .foodCategoryId(food.getFoodCategoryEntity().getId())
                .build();
    }
    //-------------------------------------------------------

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

package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.food.*;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeLeaderDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FridgeFoodService {

    private final FridgeFoodRepository fridgeFoodRepository;
    private final FridgeRepository fridgeRepository;
    private final FridgeMemberRepository fridgeMemberRepository;
    private final MemberGroupRoleRepository memberGroupRoleRepository;

    // 냉장고 메인 페이지 로드 시 식재료 정보 출력
    public List<FridgeFoodDTO> svcGetFridgeMainPage(Long memberId) {
        // 1. 메인 냉장고 ID 주석추가 진짜 마지막 추가 진짜진짜 마지막막!!
        Long mainFridgeId = svcFindMainFridgeId(memberId);

        // 2. 메인 냉장고 식재료
        List<FridgeFoodDTO> foodList = svcGetAllFridgeFoods(mainFridgeId);

        return foodList;
    }

    public List<FridgeEntity> getFridgeListByUser(Long memberId) {
        return fridgeMemberRepository.findFridgesByMemberId(memberId);
    }


    // 냉장고 참여중인 멤버 정보
    public List<FridgeGroupMemberDTO> getFridgeGroupMembers(Long fridgeId) {
        List<FridgeMemberEntity> fridgeMembers = fridgeMemberRepository.findFridgeMembersWithRolesByFridgeId(fridgeId);

        return fridgeMembers.stream()
                .map(fm -> new FridgeGroupMemberDTO(
                        fm.getId(),
                        fm.getMemberEntity().getId(),
                        fm.getMemberEntity().getNickname(),
                        fm.getMemberEntity().getEmail(),
                        fm.getJoinDate(),
                        fm.getPermissionGroupRoleList().stream()
                                .map(pgr -> pgr.getGroupRoleEntity().getRoleName())
                                .distinct() // 중복제거
                                .toList()
                ))
                .toList();
    }

    // 내가 참여하고 있는 냉장고 리스트 + 권한 정보 출력
    public List<FridgeWithRolesDTO> getFridgeListWithRoles(Long memberId) {
        List<Object[]> results = fridgeMemberRepository.findFridgeListWithRolesNative(memberId);

        return results.stream()
                .map(row -> FridgeWithRolesDTO.builder()
                        .fridgeId(((Number) row[0]).longValue())
                        .fridgeName((String) row[1])
                        .roleNames(
                                Optional.ofNullable((String) row[2])
                                        .map(roleStr -> Arrays.asList(roleStr.split(",")))
                                        .orElseGet(ArrayList::new)
                        )
                        .favoriteState((Boolean) row[3])
                        .build())
                .sorted((a, b) -> {
                    // favoriteState가 true인 것 먼저
                    boolean aFavorite = Boolean.TRUE.equals(a.getFavoriteState());
                    boolean bFavorite = Boolean.TRUE.equals(b.getFavoriteState());
                    if (aFavorite != bFavorite) {
                        return Boolean.compare(!aFavorite, !bFavorite); // true 먼저
                    }
                    // 그 다음 leader 있는 거 우선
                    boolean aIsLeader = a.getRoleNames() != null && a.getRoleNames().contains("leader");
                    boolean bIsLeader = b.getRoleNames() != null && b.getRoleNames().contains("leader");
                    return Boolean.compare(!aIsLeader, !bIsLeader);
                })
                .toList();
    }


    // 1. 즐겨찾기 냉장고 리스트
    public List<FridgeDTO> svcGetFavoriteFridgeDetail(Long memberId) {
        return fridgeMemberRepository.findByMemberEntity_IdAndFavoriteStateTrue(memberId)
                .stream()
                .map(fridgeMember -> convertToDto(fridgeMember.getFridgeEntity()))
                .collect(Collectors.toList());
    }

    // 나의 냉장고 찾기
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
                .fridgeId(fridge.getId())
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
                .build();
    }
    //-------------------------------------------------------

    // 1. Create
    public void svcCreateFridgeFood(Long fridgeId,List<FridgeFoodDTO> dtoList) {

        // 예시: FridgeEntity를 fridgeId로 가져온다고 가정
        FridgeEntity fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new IllegalArgumentException("Fridge not found"));

        for (FridgeFoodDTO dto : dtoList) {

            FridgeFoodEntity food = FridgeFoodEntity.builder()
                    .foodName(dto.getFoodName())
                    .foodAmount(dto.getFoodAmount())
                    .endDate(dto.getEndDate())
                    .chargeDate(dto.getChargeDate())
                    .saveDate(dto.getSaveDate())
                    .fridgeEntity(fridge)
                    .categoryName(dto.getCategoryName())
                    .build();
            fridgeFoodRepository.save(food);
        }

    }




    // 2. Update
    public void svcUpdateFridgeFood(List<FridgeFoodDTO> dtoList) {
        List<FridgeFoodEntity> updateList = new ArrayList<>();

        for (FridgeFoodDTO dto : dtoList) {
            FridgeFoodEntity existing = fridgeFoodRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("식재료 없음 (id: " + dto.getId() + ")"));

            FridgeEntity fridge = fridgeRepository.findById(dto.getFridgeId())
                    .orElseThrow(() -> new IllegalArgumentException("냉장고 없음 (id: " + dto.getFridgeId() + ")"));

            FridgeFoodEntity updated = FridgeFoodEntity.builder()
                    .id(existing.getId()) // ID 유지
                    .foodName(dto.getFoodName())
                    .foodAmount(dto.getFoodAmount())
                    .endDate(dto.getEndDate())
                    .chargeDate(dto.getChargeDate())
                    .saveDate(dto.getSaveDate())
                    .categoryName(dto.getCategoryName())
                    .fridgeEntity(fridge)
                    .build();

            updateList.add(updated);
        }

        fridgeFoodRepository.saveAll(updateList); // 리스트 전체 업데이트
    }

    public void deleteFridgeFoods(List<FridgeFoodDTO> updateList) {
        List<Long> idsToDelete = updateList.stream()
                .map(FridgeFoodDTO::getId)
                .filter(Objects::nonNull) // null id 방지
                .toList();

        if (!idsToDelete.isEmpty()) {
            fridgeFoodRepository.deleteAllByIdInBatch(idsToDelete);
        }
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
                .categoryName(entity.getCategoryName())
                .fridgeId(entity.getFridgeEntity().getId())
                .build();
    }
}

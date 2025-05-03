package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.food.RoleCheckboxDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupEditDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeLeaderDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.GroupRoleEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.MemberGroupRoleEntity;
import com.inmyhand.refrigerator.fridge.repository.FridgeMemberRepository;
import com.inmyhand.refrigerator.fridge.repository.GroupRoleRepository;
import com.inmyhand.refrigerator.fridge.repository.MemberGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FridgeGroupRoleService {
    private final FridgeMemberRepository fridgeMemberRepository;
    private final MemberGroupRoleRepository memberGroupRoleRepository;
    private final GroupRoleRepository groupRoleRepository;

    // 유저가 소유중인 냉장고 찾기 (leader 냉장고 찾기)
    public List<FridgeLeaderDTO> getMyLeaderFridges(Long memberId) {
        return memberGroupRoleRepository.findFridgeWhereUserIsLeader(memberId);
    }

    // 리더 냉장고 id만 가져오기
    public Long getMyLeaderFridgeId(Long memberId) {
        return memberGroupRoleRepository.findFridgeWhereUserIsLeader(memberId)
                .stream()
                .findFirst()
                .map(FridgeLeaderDTO::getFridgeId)
                .orElse(null); // 또는 예외 던지기 가능
    }

    // 멤버 역할변경
    @Transactional
    public void updateAllMemberRoles(List<FridgeGroupEditDTO> updateList) {
        List<FridgeMemberEntity> toSave = new ArrayList<>();
        System.out.println("updateAllMemberRoles 서비스 시작합니다>>>>>>>>>>>");
        for (FridgeGroupEditDTO dto : updateList) {


            // 1) fridgeMember 엔티티 로드
            FridgeMemberEntity fm = fridgeMemberRepository.findById(dto.getFridgeMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("fridgeMemberId not found: " + dto.getFridgeMemberId()));

            // 2) 기존 권한 전부 삭제
            fm.getPermissionGroupRoleList().clear();
            System.out.println("updateAllMemberRoles 서비스 시작합니다 >>>>>>>>>>>");

            // 3) 새 권한 리스트 조합
            List<String> roles = new ArrayList<>();
            // 항상 하나 들어있다고 가정
            if (dto.getRoleStatus() != null) {
                roles.add(dto.getRoleStatus().toLowerCase());  // DB roleName 은 소문자라면
            }
            if (dto.getPermissionName() != null && !dto.getPermissionName().isBlank()) {
                String[] extras = dto.getPermissionName().split("\\s*,\\s*");
                for (String r : extras) {
                    if (!r.isBlank()) roles.add(r.toLowerCase());
                }
            }

            // 4) 각 roleName 마다 MemberGroupRoleEntity 생성
            for (String roleName : roles) {
                GroupRoleEntity gr = groupRoleRepository
                    .findByRoleNameIgnoreCase(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + roleName));

                MemberGroupRoleEntity mgr = MemberGroupRoleEntity.builder()
                        .fridgeMemberEntity(fm)
                        .groupRoleEntity(gr)
                        .startDate(new Date())
                        .build();
                fm.getPermissionGroupRoleList().add(mgr);
            }

            toSave.add(fm);
        }

        // 5) 한 번에 saveAll → cascade / orphanRemoval 으로 모두 처리
        fridgeMemberRepository.saveAll(toSave);
    }


    // 그룹원 삭제
    @Transactional
    public void deleteSelectedFridgeMembers(List<FridgeGroupMemberDTO> deleteList) {
        for (FridgeGroupMemberDTO dto : deleteList) {
            Long fridgeMemberId = dto.getFridgeMemberId();

            // 1. 역할 먼저 삭제
            memberGroupRoleRepository.deleteByFridgeMemberId(fridgeMemberId);

            // 2. 멤버 삭제
            fridgeMemberRepository.deleteById(fridgeMemberId);
        }
    }

    // role 종류 전체 목록 (checkbox)
    public List<RoleCheckboxDTO> getAllRolesForCheckbox() {
    	  List<RoleCheckboxDTO> allRoles = groupRoleRepository.findAllRoleCheckboxDTO();
    	    
		    List<RoleCheckboxDTO> result = new ArrayList();
		    
		    for (RoleCheckboxDTO role : allRoles) {
		        String roleName = role.getRoleName();
		        if (!"member".equalsIgnoreCase(roleName) && !"leader".equalsIgnoreCase(roleName)) {
		            result.add(role);
		        }
		    }
		    
		    return result;
        
    }

    // 냉장고 사용 권한 관리 (편집자, 작성자, 역할 부여 처리)

    @Transactional
    public void changeUserRole(Long fridgeId, Long memberId, Long newRoleId) {
        // fridge_member 테이블에서 해당 관계 찾기
        FridgeMemberEntity fridgeMember = fridgeMemberRepository.findByFridgeEntity_IdAndMemberEntity_Id(fridgeId, memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 냉장고에 없습니다."));

        // 기존 역할 삭제 (단일 역할만 부여된다고 가정)
        memberGroupRoleRepository.deleteAll(fridgeMember.getPermissionGroupRoleList());

        // 새 역할 가져오기
        GroupRoleEntity newRole = groupRoleRepository.findById(newRoleId)
                .orElseThrow(() -> new RuntimeException("해당 역할이 존재하지 않습니다."));

        // 새 역할 할당
        MemberGroupRoleEntity newMemberGroupRole = MemberGroupRoleEntity.builder()
                .fridgeMemberEntity(fridgeMember)
                .groupRoleEntity(newRole)
                .startDate(new Date())
                .build();

        memberGroupRoleRepository.save(newMemberGroupRole);
    }
}

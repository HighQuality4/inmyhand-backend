package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.GroupRoleEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.MemberGroupRoleEntity;
import com.inmyhand.refrigerator.fridge.repository.FridgeMemberRepository;
import com.inmyhand.refrigerator.fridge.repository.GroupRoleRepository;
import com.inmyhand.refrigerator.fridge.repository.MemberGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FridgeGroupRoleService {
    private final FridgeMemberRepository fridgeMemberRepository;
    private final MemberGroupRoleRepository memberGroupRoleRepository;
    private final GroupRoleRepository groupRoleRepository;

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

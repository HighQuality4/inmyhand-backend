package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeWithRoleDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupEditDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupRequestDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeMemberPendingDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.AcceptInviteRequestDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.MemberFridgeFindDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.GroupRoleEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.MemberGroupRoleEntity;
import com.inmyhand.refrigerator.fridge.repository.FridgeMemberRepository;
import com.inmyhand.refrigerator.fridge.repository.FridgeRepository;
import com.inmyhand.refrigerator.fridge.repository.GroupRoleRepository;
import com.inmyhand.refrigerator.fridge.repository.MemberGroupRoleRepository;
import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeGroupInvitationService {

    // 냉장고 그룹원 초대, 추가, 탈퇴 관리 (유저 관리)


    private final FridgeMemberRepository fridgeMemberRepository;
    private final FridgeRepository fridgeRepository;
    private final MemberRepository memberRepository;
    private final MemberGroupRoleRepository memberGroupRoleRepository;
    private final GroupRoleRepository groupRoleRepository;

    // 유저 추가하기 (냉장고 그룹에 유저 초대)
    @Transactional
    public void addMemberToFridge(Long fridgeId, FridgeGroupRequestDTO request) {

        // 냉장고와 멤버를 DB에서 가져오기
        FridgeEntity fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new RuntimeException("해당 냉장고를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));

        // 빌더 패턴을 이용해 FridgeMemberEntity 객체 생성
        FridgeMemberEntity memberEntity = FridgeMemberEntity.builder()
                .joinDate(request.getJoinDate())
                .state(true)
                .memberEntity(member) // MemberEntity 설정
                .fridgeEntity(fridge) // FridgeEntity 설정
                .build();

        fridgeMemberRepository.save(memberEntity);
    }

    // 유저 (퇴출)삭제하기 (냉장고 그룹에서 유저 제거)
    @Transactional
    public void removeUserFromFridge(Long fridgeId, Long memberId) {
        // 해당 유저의 권한 확인하기,.,.

        // 유저를 해당 냉장고 그룹에서 제거
        FridgeMemberEntity fridgeMember = fridgeMemberRepository.findByFridgeEntity_IdAndMemberEntity_Id(fridgeId, memberId)
                .orElseThrow(() -> new RuntimeException("유저가 해당 냉장고에 없습니다."));

        fridgeMemberRepository.delete(fridgeMember);
    }


    // 유저 초대 거절하기
    @Transactional
    public void rejectInvitationFromFridge(Long fridgeId, Long memberId) {
        // 유저가 받은 초대 확인


        // 초대 받은 냉장고 그룹에서 유저 제거
//        fridgeMemberRepository.delete(fridgeMember);
    }


    // 냉장고 그룹에 속한 유저 리스트 조회
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
                                .distinct()
                                .toList()
                ))
                .sorted((a, b) -> {
                    // 1. leader 포함 여부에 따라 우선순위 부여
                    boolean aIsLeader = a.getRoleName().contains("leader");
                    boolean bIsLeader = b.getRoleName().contains("leader");

                    if (aIsLeader && !bIsLeader) {
                        return -1; // a가 leader면 앞으로
                    } else if (!aIsLeader && bIsLeader) {
                        return 1; // b가 leader면 앞으로
                    } else {
                        // 둘 다 leader거나 둘 다 아니면 joinDate 오름차순 비교
                        return a.getJoinDate().compareTo(b.getJoinDate());
                    }
                })
                .toList();
    }

    public List<FridgeMemberPendingDTO> getPendingFridgeInvites(Long memberId) {
        List<FridgeMemberPendingDTO> invites = fridgeMemberRepository.findPendingFridgeInvitesDtoByMemberId(memberId);

        return invites.stream()
                .map(invite -> new FridgeMemberPendingDTO(
                        invite.getFridgeId(),
                        invite.getFridgeName(),
                        invite.getJoinDate(),
                        invite.getState(),           // ✅ state 가져오기
                        invite.getFavoriteState()    // ✅ favoriteState 가져오기
                ))
                .sorted(Comparator.comparing(FridgeMemberPendingDTO::getJoinDate).reversed())
                .toList();
    }

    @Transactional
    public void batchUpdateRoles(List<FridgeGroupEditDTO> dtos) {
        for (FridgeGroupEditDTO dto : dtos) {
            Long fridgeMemberId = dto.getFridgeMemberId();

            // 1) 기존 역할 삭제
            memberGroupRoleRepository.deleteByFridgeMemberEntity_Id(fridgeMemberId);

            // 2) FridgeMemberEntity 조회
            FridgeMemberEntity member = fridgeMemberRepository.findById(fridgeMemberId)
                    .orElseThrow(() -> new IllegalArgumentException("FridgeMember not found: " + fridgeMemberId));

            List<Long> roleIds = new ArrayList<>();
            if (!roleIds.contains(5L)) {
                roleIds.add(5L);
            }

            String[] parts = dto.getPermissionName().split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    try {
                        roleIds.add(Long.parseLong(trimmed));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid role ID: " + trimmed, e);
                    }
                }
            }

            // 4) 새 MemberGroupRoleEntity 생성 & 저장
            List<MemberGroupRoleEntity> newRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                GroupRoleEntity role = groupRoleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("GroupRole not found: " + roleId));
                MemberGroupRoleEntity m = MemberGroupRoleEntity.builder()
                        .fridgeMemberEntity(member)
                        .groupRoleEntity(role)
                        .startDate(new Date())
                        .build();
                newRoles.add(m);
            }
            memberGroupRoleRepository.saveAll(newRoles);
        }
    }

    @Transactional
    public void inviteMemberToFridge(Long fridgeId, MemberFridgeFindDTO dto) {
        // 1) FridgeEntity 조회
        FridgeEntity fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new EntityNotFoundException("Fridge not found: " + fridgeId));

        // 2) MemberEntity 조회 (불필요한 ')' 제거)
        MemberEntity member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + dto.getMemberId()));

        // 3) FridgeMemberEntity 생성 (state=false 로 초대 상태)
        FridgeMemberEntity invitation = FridgeMemberEntity.builder()
                .state(false)               // 초대 대기 상태
                .fridgeEntity(fridge)
                .memberEntity(member)
                .joinDate(new Date())       // 초대 시각
                .favoriteState(false)       // 기본 not favorite
                .build();

        // 4) 저장
        fridgeMemberRepository.save(invitation);
    }

    // 수락시
    @Transactional
    public void acceptInvites(List<AcceptInviteRequestDTO> dtos) {
        List<FridgeMemberEntity> toSave = new ArrayList<>();

        for (AcceptInviteRequestDTO dto : dtos) {
            FridgeMemberEntity fm = fridgeMemberRepository
                    .findByFridgeEntity_IdAndMemberEntity_IdAndStateFalse(
                            dto.getFridgeId(), dto.getMemberId()
                    )
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No pending invite for member " + dto.getMemberId()
                                    + " in fridge " + dto.getFridgeId()
                    ));

            fm.setState(true);
            toSave.add(fm);
        }

        fridgeMemberRepository.saveAll(toSave);
    }

    @Transactional
    public void revokeInvites(List<AcceptInviteRequestDTO> dtos) {
        for (AcceptInviteRequestDTO dto : dtos) {
            Long fridgeId = dto.getFridgeId();
            Long memberId = dto.getMemberId();
            // (선택) 존재 여부 확인
            fridgeMemberRepository.findByFridgeEntity_IdAndMemberEntity_IdAndStateFalse(fridgeId, memberId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No pending invite to revoke for member " + memberId
                                    + " in fridge " + fridgeId
                    ));
            // 삭제
            fridgeMemberRepository.deleteByFridgeEntity_IdAndMemberEntity_IdAndStateFalse(
                    fridgeId, memberId
            );
        }
    }
}

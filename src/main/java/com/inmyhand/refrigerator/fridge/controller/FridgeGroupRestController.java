package com.inmyhand.refrigerator.fridge.controller;

import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeWithRoleDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.ChangeRoleDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupRequestDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupFacadeService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupInvitationService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fridge/{fridgeId}/group")
@RequiredArgsConstructor
public class FridgeGroupRestController {

    private final FridgeGroupRoleService fridgeGroupRoleService;
    private final FridgeGroupFacadeService fridgeGroupFacadeService;
    private final FridgeGroupInvitationService fridgeGroupInvitationService;

    // 유저 추가하기
    @PostMapping
    public ResponseEntity<Void> addMemberToFridge(@PathVariable Long fridgeId,
                                                @RequestBody FridgeGroupRequestDTO request) {
        fridgeGroupInvitationService.addMemberToFridge(fridgeId, request);
        return ResponseEntity.ok().build();
    }

    // 그룹원 (탈퇴)삭제하기
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<Void> removeMemberFromFridge(@PathVariable Long fridgeId,
                                                     @PathVariable Long memberId) {
        fridgeGroupInvitationService.removeUserFromFridge(fridgeId, memberId);
        return ResponseEntity.ok().build();
    }

    // 그룹원 거절 삭제하기
    @DeleteMapping("/reject/{memberId}")
    public ResponseEntity<Void> rejectInvitationFromFridge(@PathVariable Long fridgeId,
                                                     @PathVariable Long memberId) {
        fridgeGroupInvitationService.rejectInvitationFromFridge(fridgeId, memberId);
        return ResponseEntity.ok().build();

    }

    // 한 냉장고의 참여중인 유저+권한 정보 전체 출력하기
    @GetMapping
    public ResponseEntity<List<FridgeGroupMemberDTO>> getAllMembersInFridge(@PathVariable Long fridgeId) {
        return ResponseEntity.ok(fridgeGroupInvitationService.getAllMembers(fridgeId));
    }


    // 유저 권한 변경하기
    @PutMapping("/{memberId}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long fridgeId,
                                               @PathVariable Long memberId,
                                               @RequestBody ChangeRoleDTO request) {
        fridgeGroupRoleService.changeUserRole(fridgeId, memberId, request.getNewRoleId());
        return ResponseEntity.ok().build();
    }


    // 유저 이름으로 단일 유저 정보 출력하기
    @GetMapping("/search")
    public ResponseEntity<FridgeGroupMemberDTO> getMemberByName(@PathVariable Long fridgeId,
                                                                @RequestParam String name) {
        return ResponseEntity.ok(fridgeGroupInvitationService.findMemberByName(fridgeId, name));
    }


    /**
     * 특정 멤버가 참여한 냉장고 목록과 역할을 반환하는 API
     * @param memberId 멤버 ID
     * @return 냉장고 이름과 역할 이름을 담은 DTO 목록
     */
//    @GetMapping("/roles")
//    public List<FridgeWithRoleDTO> getFridgesWithRoleNamesByMember(@RequestParam Long memberId) {
//        // 서비스 호출
//        return fridgeGroupInvitationService.getFridgesWithRoleNamesByMember(memberId);
//    }
}

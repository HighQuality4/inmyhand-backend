package com.inmyhand.refrigerator.fridge.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeWithRolesDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.RoleCheckboxDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.*;
import com.inmyhand.refrigerator.fridge.domain.dto.search.SearchFridgeDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupFacadeService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupInvitationService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupRoleService;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fridge/role")
@RequiredArgsConstructor
@Slf4j
public class FridgeGroupRestController {

    private final FridgeGroupRoleService fridgeGroupRoleService;
    private final FridgeGroupFacadeService fridgeGroupFacadeService;
    private final FridgeGroupInvitationService fridgeGroupInvitationService;



    @PostMapping("/checkbox")
    public ResponseEntity<?> getRolesForCheckbox() {
        List<RoleCheckboxDTO> roleList = fridgeGroupRoleService.getAllRolesForCheckbox();
        return ResponseEntity.ok(Map.of( "roleList", roleList ));
    }

    // 초대 대기중인 냉장고 리스트 가져오기
    @PostMapping("/pending/groupList")
    public  ResponseEntity<?> getPendingInvites(DataRequest dataRequest) {
        SearchFridgeDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "dmFridgeParam", SearchFridgeDTO.class);
//        long memberId = singleClass.getMemberId();
//        log.info(" 멤버 아이디 결과 값 >>>>>"+ memberId);

        long memberId = 3L;
        List<FridgeMemberPendingDTO> pendingGroupList = fridgeGroupInvitationService.getPendingFridgeInvites(memberId);
        return ResponseEntity.ok(Map.of( "inviteList", pendingGroupList ));
    }
    // /api/fridge/role/group/edit
    @PostMapping("/group/edit")
    public  ResponseEntity<?> editGroupList(DataRequest dataRequest) {


        // ===================== 1. 식재료 정보 수정하는 로직 =====================
        // 수정할 리스트
        List<FridgeGroupEditDTO> updateList = ConverterClassUtil.getClassList(dataRequest, "updateGroupList",FridgeGroupEditDTO.class);
        // 수정로직
        fridgeGroupRoleService.updateAllMemberRoles(updateList);

        for (FridgeGroupEditDTO dto : updateList) {
            log.info(">> fridgeMemberId: {}, memberId: {}, getRoleStatus: {},  joinDate: {}, getPermissionName: {}",
                    dto.getFridgeMemberId(),
                    dto.getMemberId(),
                    dto.getRoleStatus(),
                    dto.getJoinDate(),
                    dto.getPermissionName()
            );
        }
        // ===================== 2. 식재료 삭제 로직 =====================
        // 삭제할 리스트
        List<FridgeGroupMemberDTO> deleteList = ConverterClassUtil.getClassList(dataRequest, "deleteGroupList",FridgeGroupMemberDTO.class);

        // 삭제 로직
        fridgeGroupRoleService.deleteSelectedFridgeMembers(deleteList);

        return  ResponseEntity.ok("ok");
    }
//   leader 냉장고 찾기
//    @GetMapping("/member/{memberId}/leader-fridges")
//    public List<FridgeLeaderDTO> getLeaderFridges(@PathVariable Long memberId) {
//        return fridgeGroupRoleService.getMyLeaderFridges(memberId);
//    }

    
   /* // 유저 추가하기
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
        return ResponseEntity.ok(fridgeGroupInvitationService.getFridgeGroupMembers(fridgeId));
    }


    // 유저 권한 변경하기
    @PutMapping("/{memberId}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long fridgeId,
                                               @PathVariable Long memberId,
                                               @RequestBody ChangeRoleDTO request) {
        fridgeGroupRoleService.changeUserRole(fridgeId, memberId, request.getNewRoleId());
        return ResponseEntity.ok().build();
    }*/


//    // 유저 이름으로 단일 유저 정보 출력하기
//    @GetMapping("/search")
//    public ResponseEntity<FridgeGroupMemberDTO> getMemberByName(@PathVariable Long fridgeId,
//                                                                @RequestParam String name) {
//        return ResponseEntity.ok(fridgeGroupInvitationService.findMemberByName(fridgeId, name));
//    }


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

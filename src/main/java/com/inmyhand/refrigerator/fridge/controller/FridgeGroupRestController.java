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



}

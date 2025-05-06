package com.inmyhand.refrigerator.admin.controller;

import java.util.List;
import java.util.Map;

import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.service.AdminService;
import com.inmyhand.refrigerator.util.ConverterClassUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    /**
     * key : ad1 value : List<MemberEntityDto>
     *
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView() {
        return ResponseEntity
                .ok(Map.of("content", adminService.findAllMembers()));
    }

    /**
     * 유저 업데이트 key : ad1 value : List<MemberEntityDto>
     *
     * @return
     */
    @PutMapping("/user-update")
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView2(
            DataRequest dataRequest) {

        List<MemberEntityDto> classList = ConverterClassUtil
                .getClassList(dataRequest, "content", MemberEntityDto.class);
        adminService.updateMember(classList);

        return ResponseEntity
                .ok(Map.of("content", adminService.findAllMembers()));
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> userRecipe(
            @RequestParam(required = false, value = "page", defaultValue = "0") int pageId,
            @RequestParam(required = false, value = "name") String name,
            @PathVariable("id") Long id) {

        log.info("pageId: " + pageId + ", name: " + name + ", id: " + id);

        return ResponseEntity.ok(adminService.findAllAdminRecipeInfo(id, name,
                PageRequest.of(pageId, 30)));
    }


    @PostMapping("/user/search")
    public ResponseEntity<?> userSearch(@RequestParam(required = false, value = "page", defaultValue = "0") int pageId,
                                        @ModelAttribute MemberCustomQueryDTO memberCustomQueryDTO) {

        return ResponseEntity.ok(adminService.findMemberDTOSearch(PageRequest.of(pageId, 30), memberCustomQueryDTO));
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("어드민 체크 실행");
        if(customUserDetails != null && customUserDetails.getUserId() != null && customUserDetails.getUserId() == 1) {
            log.info("admin : 1111");
            return ResponseEntity.ok(Map.of("adcheck",Map.of("check","1")));
        } else {
            log.info("admin : 0000");
            return ResponseEntity.ok(Map.of("adcheck",Map.of("check","0")));
        }
    }


}

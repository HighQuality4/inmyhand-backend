package com.inmyhand.refrigerator.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.ParameterGroup;
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
     * key : ad1
     * value : List<MemberEntityDto>
     * @return
     */
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView() {
        return ResponseEntity.ok(Map.of("getusers", adminService.findAllMembers()));
    }

    /**
     * 유저 업데이트
     * key : ad1
     * value : List<MemberEntityDto>
     * @return
     */
    @PostMapping("/user-update")
    @ResponseBody
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView2(DataRequest dataRequest) {

    	List<MemberEntityDto> classList = ConverterClassUtil.getClassList(dataRequest, "getusers", MemberEntityDto.class);
        adminService.updateMember(classList);

        return ResponseEntity.ok(Map.of("getusers",  adminService.findAllMembers()));
    }
}

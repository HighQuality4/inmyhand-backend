package com.inmyhand.refrigerator.admin.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.ParameterGroup;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.service.AdminService;
import com.inmyhand.refrigerator.test.TestDTO;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/")
    public String adminPage() {
        return "admin/user-view";
    }


    /**
     * key : ad1
     * value : List<MemberEntityDto>
     * @return
     */
    @PostMapping("/api/admin/user-all-view")
    @ResponseBody
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView() {
        return ResponseEntity.ok(Map.of("ad1", adminService.findByMemberAll()));
    }

    /**
     * 유저 업데이트
     * key : ad1
     * value : List<MemberEntityDto>
     * @return
     */
    @PostMapping("/api/admin/user-update")
    @ResponseBody
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView2(DataRequest dataRequest) {

        // 1
        adminService.updateMember(ConverterClassUtil
                .getSingleClass(dataRequest, "dm1",MemberEntityDto.class));
        // 2
//        MemberEntityDto dm1 = ConverterClassUtil.getSingleClass(dataRequest, "dm1", MemberEntityDto.class);
//        adminService.updateMember(dm1);

        // 3
//        ParameterGroup dm2 = dataRequest.getParameterGroup("dm1");
//        MemberEntityDto beanData = (MemberEntityDto)dm2.getBeanData(MemberEntityDto.class);
//        adminService.updateMember(beanData);


        return ResponseEntity.ok(Map.of("ad1", adminService.findByMemberAll()));
    }
}

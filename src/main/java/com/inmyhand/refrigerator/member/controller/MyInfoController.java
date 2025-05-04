package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.ChangePwDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyInfoDTO;
import com.inmyhand.refrigerator.member.service.*;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myInfo/")
public class MyInfoController {

    private final MyInfoServiceImpl myInfoService;

    @PostMapping("/getMyInfo")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> myInfo = myInfoService.findMyInfo(userDetails.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("dmGetMyInfo", myInfo);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/setMyInfo") //db update
    public ResponseEntity<?> setMyInfo(DataRequest dataRequest) {

        MyInfoDTO myInfoDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmSetMyInfo", MyInfoDTO.class);
        boolean result = myInfoService.updateMyInfo(myInfoDTO);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {

        boolean success = myInfoService.resetPassword(email);

        return ResponseEntity.ok(success);
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, DataRequest dataRequest) {
        ChangePwDTO changePwDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmChangePw", ChangePwDTO.class);
        String password = changePwDTO.getPassword1();
        boolean result = myInfoService.changePassword(customUserDetails.getEmail(), password);

        return ResponseEntity.ok(result);
    }


}

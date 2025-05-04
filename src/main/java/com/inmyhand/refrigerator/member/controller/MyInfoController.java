package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.ChangePwDTO;
import com.inmyhand.refrigerator.member.domain.dto.EmailAuthDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyInfoDTO;
import com.inmyhand.refrigerator.member.service.*;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myInfo/")
public class MyInfoController {

    private final MyInfoServiceImpl myInfoService;
    private final LogoutServiceImpl logoutService;

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
    public ResponseEntity<?> resetPassword(DataRequest dataRequest) {

        EmailAuthDTO emailAuthDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmEmail", EmailAuthDTO.class);

        String email = emailAuthDTO.getEmail();
        System.out.println(email);
        boolean success = myInfoService.resetPassword(email);

        return ResponseEntity.ok(success);
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            DataRequest dataRequest) {
        Long userId = customUserDetails.getUserId();
        String email = customUserDetails.getEmail();

        ChangePwDTO changePwDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmChangePw", ChangePwDTO.class);
        System.out.println(changePwDTO);

        String newPassword = changePwDTO.getPassword1();
        boolean isChanged = myInfoService.changePassword(email, newPassword);

        HttpHeaders headers = new HttpHeaders();

        if (isChanged) {
            // 로그아웃 처리 및 access_token 쿠키 제거
            logoutService.logout(userId);

            ResponseCookie deleteAccessToken = ResponseCookie.from("access_token", "")
                    .path("/")
                    .maxAge(0)
                    .httpOnly(true)
                    .sameSite("Strict")
                    //.secure(true)
                    .build();

            headers.add("Set-Cookie", deleteAccessToken.toString());
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(isChanged);
    }



}

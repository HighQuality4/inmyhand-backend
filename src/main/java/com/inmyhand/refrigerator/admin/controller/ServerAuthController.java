package com.inmyhand.refrigerator.admin.controller;


import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
@Slf4j
public class ServerAuthController {

    @GetMapping("/check")
    public ResponseEntity<Void> checkAuth(@AuthenticationPrincipal CustomUserDetails custom) {



        try {
            Long userId = custom.getUserId();
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else if (userId == 1L) {
                log.info("어드민 유저 로그인 SERVER 로그인");
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


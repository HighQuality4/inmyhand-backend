package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.dto.LogoutRequestDTO;
import com.inmyhand.refrigerator.member.service.LoginServiceImpl;
import com.inmyhand.refrigerator.member.service.LogoutServiceImpl;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LogInAndOutController {

    private final LoginServiceImpl loginService;
    private final LogoutServiceImpl logoutService;

    @PostMapping("/login/test")
    public ResponseEntity<LoginResponseDTO> ctlLoginTest(@RequestBody LoginRequestDTO loginDto, HttpServletResponse response) {
        loginService.login(loginDto, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> ctlLogin(DataRequest dataRequest, HttpServletResponse response) {

        LoginRequestDTO loginRequestDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmLogin", LoginRequestDTO.class);
        boolean login = loginService.login(loginRequestDTO, response);
        log.info("Login status: {}", login);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(DataRequest datarequest) {

        LogoutRequestDTO logoutRequestDTO = ConverterClassUtil.getSingleClass(datarequest, "dmLogout", LogoutRequestDTO.class);

        Long userId = logoutRequestDTO.getId();

        // 해당 userId로 리프레시 토큰 조회 후 삭제
        logoutService.logout(userId);

        // access_token 쿠키도 제거
        ResponseCookie deleteAccessToken = ResponseCookie.from("access_token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok() //응답 없음: 204 code
                .header(HttpHeaders.SET_COOKIE, deleteAccessToken.toString())
                .body("로그아웃 완료");
    }

}

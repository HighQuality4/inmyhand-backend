package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.service.LoginServiceImpl;
import com.inmyhand.refrigerator.member.service.LogoutServiceImpl;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LogInAndOutController {

    private final LoginServiceImpl loginService;
    private final LogoutServiceImpl logoutService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login/test")
    public ResponseEntity<?> ctlLoginTest(Principal principal) {
        String name = principal.getName();
        log.info("username = {}", name);

        return ResponseEntity.ok(name);
    }

    @PostMapping("/login")
    public ResponseEntity<?> ctlLogin(DataRequest dataRequest, HttpServletResponse response) {

        LoginRequestDTO loginRequestDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmLogin", LoginRequestDTO.class);
        boolean login = loginService.login(loginRequestDTO, response);
        log.info("Login status: {}", login);
        Map<String, Object> result = new HashMap<>();
        result.put("success", login); // true or false

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> ctlLogout(@CookieValue(value = "access_token", required = false) String accessToken) {

        Long userId = jwtTokenUtil.getUserIdFromToken(accessToken);

        // 해당 userId로 리프레시 토큰 조회 후 삭제
        Boolean logout = logoutService.logout(userId);

        HttpHeaders headers = new HttpHeaders();
        if (logout) {
            // access_token 쿠키도 제거
            ResponseCookie deleteAccessToken = ResponseCookie.from("access_token", "")
                    .path("/")
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(true)
                    .build();

            headers.add(HttpHeaders.SET_COOKIE, deleteAccessToken.toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(logout);
        } else {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(logout);
        }
    }

}

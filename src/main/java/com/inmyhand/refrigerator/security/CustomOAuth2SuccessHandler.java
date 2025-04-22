package com.inmyhand.refrigerator.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = user.getAccessToken();
        System.out.println("accessToken: " + accessToken);
        // HTTPOnly 쿠키 설정
        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // production이면 true로 유지
                .sameSite("None") // 또는 "Strict" / "None" (CORS 환경 따라)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
        // 프론트엔드 페이지로 리다이렉트
        response.sendRedirect("http://localhost:7079/auth/login"); // 실제 프론트 페이지 주소
    }
}

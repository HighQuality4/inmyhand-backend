package com.inmyhand.refrigerator.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
                .sameSite("Lax") // 또는 "Strict" / "None" (CORS 환경 따라)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        //request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");

        // JSESSIONID 쿠키 명시적 제거
        ResponseCookie jSessionIdCookie = ResponseCookie.from("JSESSIONID", "")
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", jSessionIdCookie.toString());

        // 세션 무효화 - OAuth2 로그인 완료 후 세션 정보가 더 이상 필요 없음
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 프론트엔드 페이지로 리다이렉트
        response.sendRedirect("http://localhost:7079/auth/login"); // 실제 프론트 페이지 주소
    }
}

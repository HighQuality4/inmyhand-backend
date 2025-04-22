package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.RefreshTokenEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final LoginRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = user.getAccessToken();
        // HTTPOnly 쿠키 설정
        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // production이면 true로 유지
                .sameSite("Lax") // 또는 "Strict" / "None" (CORS 환경 따라)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // 프론트엔드 페이지로 리다이렉트
        response.sendRedirect("http://127.0.0.1:52194/inmyhand/clx-src/app/login/login.clx.html"); // 실제 프론트 페이지 주소
    }
}

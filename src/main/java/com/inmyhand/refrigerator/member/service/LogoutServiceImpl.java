package com.inmyhand.refrigerator.member.service;

import com.google.api.gax.rpc.ApiException;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.BasicResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtTokenUtil jwtTokenUtil;

    public void logout(String accessToken, String refreshToken) {
        //  1. AccessToken 검증
        if(!jwtTokenUtil.validateToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다!");
        }

        // 2. AccessToken에서 사용자 식별(userId)
        String userIdStr = jwtTokenUtil.getUsernameFromToken(accessToken);
        Long userId = Long.parseLong(userIdStr);

        // 3. DB에서 해당 사용자의 refreshToken 삭제

        // 4. AccessToken을 Redis 블랙리스트에 등록

    }
}

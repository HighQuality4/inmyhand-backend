package com.inmyhand.refrigerator.security.jwt;

import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.common.redis.RedisUtil;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;
    private final RedisKeyManager redisKeyManager;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String path = request.getRequestURI();

// 1. 먼저 Authorization 헤더에서 꺼내기
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

// 2. Authorization 헤더가 없으면 쿠키에서 access_token 찾아보기
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

// 3. 토큰이 여전히 없다면 그냥 필터 통과
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 유효성 검증
            if (jwtTokenUtil.validateToken(token)) {
                // 토큰에서 사용자명 추출
                String username = jwtTokenUtil.getUsernameFromToken(token);
                Long userId = jwtTokenUtil.getUserIdFromToken(token);

                //블랙리스트 키값 만들어서 조회
                String blackListKey = redisKeyManager.getBlackListKey(userId);

                String blacklistedToken = redisUtil.get(blackListKey);

                if (blacklistedToken != null && blacklistedToken.equals(token)) {
                    log.warn("블랙리스트에 등록된 토큰입니다: {}", token);
                    throw new SecurityException("로그아웃된 토큰입니다");
                }

                // UserDetails 객체 가져오기
                log.warn("🎯 username from token: {}", username);
                CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                log.info(userDetails.getUsername());
                log.info("" + userDetails.getUserId());

                // 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

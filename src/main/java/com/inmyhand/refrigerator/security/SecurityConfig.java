package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;


/**
 * 시큐리티 설정
 * csrf 공격 방어 활성화 시켜야함
 * authorizeHttpRequests 유저 권한에 따라 페이지 접근 설정
 * oauth2Login Oauth 설정 활성화 해야함
 * 아래는 예시 이므로 직접 구현하기
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .headers(headers -> headers
//                        // 최신 OWASP 권장사항에 따라 X-XSS-Protection 헤더를 비활성화
//                        .xssProtection(xss -> xss
//                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.DISABLED))
//                        // CSP는 XSS 방지에 더 효과적이므로 유지
//                        .contentSecurityPolicy(csp ->
//                                csp.policyDirectives("script-src 'self'"))
//                        // 클릭재킹 방지를 위한 DENY 설정 유지
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
//                )
                // JWT 필터 추가
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
        ;
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

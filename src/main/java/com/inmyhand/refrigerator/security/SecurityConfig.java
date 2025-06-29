package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Oauth2UserDetailService oauth2UserDetailService, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 어드민 전용
                        .requestMatchers("/users/**").hasAnyRole("FREETIER", "PAID", "ADMIN") // 유저 또는 어드민
                        .anyRequest().permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(customAuthorizationRequestResolver(clientRegistrationRepository))
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserDetailService)
                        )
                        .successHandler(customOAuth2SuccessHandler)
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                )
                .requestCache(RequestCacheConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                //.formLogin(withDefaults()) //테스트 용도
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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { //CORS 커스터마이징
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(allowedOrigins); // or "*"
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true); // 쿠키 사용할 경우
        config.addExposedHeader("Content-Disposition"); //이거 추가해야지 "Content-Disposition" 콘솔에러가 발생 안함

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //처음 로그인 할 때만 동의, 나중에 로그인 할 때는 자동으로
    private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        // 여기서 prompt 파라미터 수정
        resolver.setAuthorizationRequestCustomizer(customizer ->
                customizer.additionalParameters(params -> {
                    params.remove("prompt"); // 또는 params.put("prompt", "select_account");
                })
        );

        return resolver;
    }
}

package com.inmyhand.refrigerator.security;

import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.common.redis.RedisUtil;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberRoleEntity;
import com.inmyhand.refrigerator.member.domain.entity.RefreshTokenEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberRole;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class Oauth2UserDetailService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;
    private final RedisKeyManager redisKeyManager;
    private final LoginRepository loginRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String name = null;
        String nickname = null;
        String accessToken = null;
        String refreshToken = null;
        String provider = registrationId.toUpperCase();

        if (registrationId.equals("naver")) { //네이버
            Map<String, Object> response = (Map<String, Object>) attributes.get("response"); //네이버는 사용자 정보가 response 안에 있음.
            email = (String) response.get("email");
            nickname = (String) response.get("nickname");
            name = (String) response.get("name");
        } else if (registrationId.equals("google")) { //구글
            email = (String) attributes.get("email");
            nickname = (String) attributes.get("name");
            name = (String) attributes.get("name");
        } else if (registrationId.equals("kakao")) { //카카오
            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) response.get("profile");
            email = (String) response.get("email");
            nickname = (String) profile.get("nickname");
            name = (String) profile.get("name");
        }

        MemberEntity member = loginRepository.findByEmail(email).orElse(null); //멤버 있는지 확인
        RefreshTokenEntity existedToken = refreshTokenRepository.findByMemberEntity(member).orElse(null); //리프레시 토큰 있는지 확인

        if (member == null) { // 멤버 조회결과가 없다면
            member = MemberEntity.builder()
                    .email(email)
                    .memberName(name)
                    .nickname(nickname)
                    .providerId(provider)
                    .regdate(new Date())
                    .build();

            memberRepository.save(member); //멤버 저장

            MemberRoleEntity role = new MemberRoleEntity();
            role.setUserRole(MemberRole.freetier); // 기본값이지만 명시적 지정
            role.setMemberEntity(member);
            member.getMemberRoleList().add(role); // 양방향 연관관계 유지

            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setTokenValue(jwtTokenUtil.generateRefreshToken(member)); //리프레시 토큰 발급
            refreshTokenEntity.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7))); // 예: 1주 유효
            refreshTokenEntity.setMemberEntity(member);
            refreshTokenRepository.save(refreshTokenEntity);
            refreshTokenRepository.flush();

        } else { // 멤버조회 결과가 있다면
            if (existedToken == null) { //거기에 리프레시 토큰이 없다면
                existedToken = new RefreshTokenEntity();
                existedToken.setMemberEntity(member); //리프레시 토큰 엔티티에 멤버 정보 넣기
            }
            //리프레시 토큰 값만 갱신
            refreshToken = jwtTokenUtil.generateRefreshToken(member); //리프레시 토큰 발급
            existedToken.setTokenValue(refreshToken); //리프레시 토큰 엔티티에 발급한 토큰 넣기
            existedToken.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7))); //만료기한 설정해서 넣기
            refreshTokenRepository.save(existedToken); //리프레시 토큰 엔티티 저장
            refreshTokenRepository.flush();
        }

        String redisKey = redisKeyManager.getLoginKey(member.getId());
        Long expiredTime = jwtTokenUtil.getRemainingTimeFromToken(refreshToken);

        try {
            redisUtil.set(redisKey, refreshToken, expiredTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis 저장 실패 - RefreshToken: {}", redisKey, e);
        }

        accessToken = jwtTokenUtil.generateAccessToken(member);
        List<GrantedAuthority> authorities = member.getMemberRoleList().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getUserRole().name().toUpperCase()))
                .collect(Collectors.toList());

        return new CustomOAuth2User(
                member,
                oAuth2User.getAttributes(),
                accessToken
        );

    }
}

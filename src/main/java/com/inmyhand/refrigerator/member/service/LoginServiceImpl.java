package com.inmyhand.refrigerator.member.service;

import com.google.api.gax.rpc.NotFoundException;
import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.common.redis.RedisUtil;
import com.inmyhand.refrigerator.error.exception.NotMatchPasswordException;
import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.RefreshTokenEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final RedisKeyManager redisKeyManager;

    @Transactional
    @Override
    public boolean login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        Optional<MemberEntity> optionalMember = loginRepository.findByEmail(loginRequestDTO.getEmail());
        if (optionalMember.isEmpty()) {
            return false;
        }

        MemberEntity member = optionalMember.get();

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            return false;
        }


        String accessToken = jwtTokenUtil.generateAccessToken(member);
        String refreshToken = jwtTokenUtil.generateRefreshToken(member);

        String redisKey = redisKeyManager.getLoginKey(member.getId());
        Long expiredTime = jwtTokenUtil.getRemainingTimeFromToken(accessToken);

        //기존 리프레시 토큰이 있으면 토큰 값만 수정(나중에 Redis에서도 처리하도록 변경할 것!)
        RefreshTokenEntity existingToken = refreshTokenRepository.findByMemberEntity(member).orElse(null);

        if (existingToken != null) {
            existingToken.setTokenValue(refreshToken);
            existingToken.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
            refreshTokenRepository.save(existingToken);
        } else {
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setTokenValue(refreshToken);
            refreshTokenEntity.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
            refreshTokenEntity.setMemberEntity(member);

            //다시 발급한 리프레시 토큰을 DB에 저장(이것도 레디스에 반영해야 함)
            refreshTokenRepository.save(refreshTokenEntity);
        }

        try {
            redisUtil.set(redisKey, refreshToken, expiredTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis 저장 실패 - RefreshToken: {}", redisKey, e);
        }

        // 6. AccessToken을 HTTP-Only Secure 쿠키로 클라이언트에 전송
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)           // JS에서 접근 불가
                //.secure(true)             // HTTPS 환경에서만 전송
                .sameSite("Strict")       // CSRF 방지
                .path("/")                // 모든 경로에서 접근 가능
                .maxAge(Duration.ofMinutes(30))
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());

        return true;
    }

}

package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.RefreshTokenEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        // 비밀번호 매칭 프로세스 추가하기!
        MemberEntity member = loginRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!member.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(member);
        String refreshToken = jwtTokenUtil.generateRefreshToken(member);

        //기존 리프레시 토큰이 있으면 토큰 값만 수정(나중에 Redis에서도 처리하도록 변경할 것!)
        RefreshTokenEntity existingToken = refreshTokenRepository.findByMemberEntity(member);

        if (existingToken != null) {
            existingToken.setTokenValue(refreshToken);
            refreshTokenRepository.save(existingToken);
        } else {
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setTokenValue(refreshToken);
            refreshTokenEntity.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
            refreshTokenEntity.setMemberEntity(member);

            //다시 발급한 리프레시 토큰을 DB에 저장(이것도 레디스에 반영해야 함)
            refreshTokenRepository.save(refreshTokenEntity);
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

        return LoginResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}

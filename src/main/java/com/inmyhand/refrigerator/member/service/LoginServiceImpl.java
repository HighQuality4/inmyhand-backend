package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.RefreshTokenEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // 비밀번호 매칭 프로세스 추가하기!
        MemberEntity member = loginRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!member.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(member);
        String refreshToken = jwtTokenUtil.generateRefreshToken(member);

        //기존 리프레시 토큰이 있으면 삭제(나중에 Redis에서도 처리하도록 변경할 것!)
        RefreshTokenEntity existingToken = refreshTokenRepository.findByMemberEntity(member);
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setTokenValue(refreshToken);
        refreshTokenEntity.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
        refreshTokenEntity.setMemberEntity(member);

        //다시 발급한 리프레시 토큰을 DB에 저장(이것도 레디스에 반영해야 함)
        refreshTokenRepository.save(refreshTokenEntity);

        return LoginResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }



}

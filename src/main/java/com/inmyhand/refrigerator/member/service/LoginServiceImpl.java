package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.LoginRepository;
import com.inmyhand.refrigerator.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // 비밀번호 매칭 프로세스 추가하기!
        MemberEntity member = loginRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!member.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(member);
        System.out.println(accessToken);
        String refreshToken = jwtTokenUtil.generateRefreshToken(member);
        System.out.println(refreshToken);

        return LoginResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }



}

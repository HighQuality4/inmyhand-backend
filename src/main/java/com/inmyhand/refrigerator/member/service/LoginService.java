package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface LoginService {
    boolean login(LoginRequestDTO loginRequestDTO, HttpServletResponse response);
}

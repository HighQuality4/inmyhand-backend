package com.inmyhand.refrigerator.member.controller;

import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.service.LoginServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemeberController {

    private final LoginServiceImpl loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> ctlLogin(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        LoginResponseDTO member = loginService.login(loginRequestDTO, response);
        return ResponseEntity.ok().body(member);
    }
}

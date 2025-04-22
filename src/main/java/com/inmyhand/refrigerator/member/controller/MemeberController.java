package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.member.domain.dto.LoginResponseDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.service.LoginServiceImpl;
import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import com.inmyhand.refrigerator.member.service.RegisterServiceImpl;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemeberController {

    private final LoginServiceImpl loginService;
    private final RegisterServiceImpl registerService;

    @PostMapping("/login/test")
    public ResponseEntity<LoginResponseDTO> ctlLoginTest(@RequestBody LoginRequestDTO loginDto, HttpServletResponse response) {
        LoginResponseDTO responseDTO = loginService.login(loginDto, response);
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> ctlLogin(DataRequest loginRequestDTO, HttpServletResponse response) {

        System.out.println("받은 값: " + loginRequestDTO.toString());
        LoginResponseDTO loginResponseDTO = ConverterClassUtil.getSingleClass(loginRequestDTO, "dmLogin", LoginResponseDTO.class);
        System.out.println(loginResponseDTO.toString());
        //LoginResponseDTO member = loginService.login(loginRequestDTO, response);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<String> ctlRegister(@RequestBody MemberEntity memberEntity, HttpServletResponse response) {
        registerService.registerMember(memberEntity);
        return ResponseEntity.ok("가입이 완료되었습니다!");
    }
}

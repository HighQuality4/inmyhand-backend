package com.inmyhand.refrigerator.member.controller;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.service.MemberService;
import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemeberRestController {

    private final MemberServiceImpl memberService;

    @PostMapping("/register")
    public ResponseEntity<String> ctlRegister(@RequestBody MemberDTO memberDTO) {
        memberService.registerLocal(memberDTO);
        return ResponseEntity.ok("가입이 완료되었습니다!");
    }

//    @PostMapping("/profile_img")
//    public String ctlGetProfileImg(@RequestParam Long memberId) {
//        return memberService.getMemberProfileImage(memberId);
//    }
}

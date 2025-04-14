package com.inmyhand.refrigerator.member.controller;

import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemeberRestController {

    private final MemberServiceImpl memberService;

//    @PostMapping("/profile_img")
//    public String ctlGetProfileImg(@RequestParam Long memberId) {
//        return memberService.getMemberProfileImage(memberId);
//    }
}

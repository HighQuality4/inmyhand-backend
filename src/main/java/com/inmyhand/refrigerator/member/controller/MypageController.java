package com.inmyhand.refrigerator.member.controller;

import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.member.service.MemberService;
import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberServiceImpl memberService;

    @PostMapping("/api/profile")
    public ResponseEntity<?> getProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        System.out.println("컨트롤러 접속 성공");

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", userDetails.getNickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        profile.put("createdAt", sdf.format(userDetails.getRegdate()));

        Map<String, Object> result = new HashMap<>();
        result.put("dmProfile", profile);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/myrefre")
    public ResponseEntity<?> getMyRefreInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();

        List<MyFoodInfoDTO> flist = memberService.MyRefreInfo(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("dmMyRef", flist);

        return ResponseEntity.ok(flist);
    }
}

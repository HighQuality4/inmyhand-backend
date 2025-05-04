package com.inmyhand.refrigerator.member.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.EmailAuthDTO;
import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.service.EmailAuthService;
import com.inmyhand.refrigerator.member.service.MemberServiceImpl;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemeberRestController {

    private final MemberServiceImpl memberService;
    private final JavaMailSender mailSender;
    private final EmailAuthService emailAuthService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> ctlRegister(DataRequest dataRequest) {
    	
    	MemberDTO memberDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmRegister", MemberDTO.class);
    	
        boolean memberSaved = memberService.registerLocal(memberDTO);
        return ResponseEntity.ok(memberSaved);
    }

    @PostMapping("/auth/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("테스트 메일입니다");
        message.setText("이건 테스트 이메일 입니다. 연결 성공!");

        mailSender.send(message);
        return ResponseEntity.ok("메일 전송 성공");
    }

    @PostMapping("/auth/verify-email")
    public ResponseEntity<String> sendEmailVerification(DataRequest dataRequest) {

        EmailAuthDTO emailAuthDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmEmailAuth", EmailAuthDTO.class);

        emailAuthService.sendEmailVerification(emailAuthDTO.getEmail());

        return ResponseEntity.ok("이메일로 인증 링크를 보냈습니다.");
    }

    @PostMapping("/auth/verify-code")
    public ResponseEntity<?> emailVerifyCode(DataRequest dataRequest) {
        EmailAuthDTO emailAuthDTO = ConverterClassUtil.getSingleClass(dataRequest, "dmEmailCode", EmailAuthDTO.class);
        int code = Integer.parseInt(emailAuthDTO.getCode());
        String email = emailAuthDTO.getEmail();
        boolean emailAuth = emailAuthService.verifyEmailCode(code, email);
        return ResponseEntity.ok(emailAuth);
    }

    @PostMapping("/check/local")
    public ResponseEntity<?> checkLocal(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        boolean result = memberService.checkLocalOrElse(userId);

        return ResponseEntity.ok(result);
    }



//    @GetMapping("/auth/verify-email")
//    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
//        String emailAuthKey = redisKeyManager.getEmailAuthKey(token);
//        String email = redisUtil.get(emailAuthKey);
//
//        if (email == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않거나 만료된 인증 번호입니다.");
//        }
//
//        redisUtil.delete(emailAuthKey);
//
//        redisUtil.set(redisKeyManager.getEmailVerifiedKey(email), "true", 30, TimeUnit.MINUTES);
//        ResponseCookie cookie = ResponseCookie.from("email_verified", "true")
//                .path("/")
//                .httpOnly(false)
//                .maxAge(Duration.ofMinutes(5))
//                .build();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(Map.of("status", "SUCCESS", "message", "이메일 인증 완료"));

//        return ResponseEntity.status(HttpStatus.FOUND)
//                .header(HttpHeaders.LOCATION, "/users/register")  // 회원가입 페이지로 이동
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())  // 쿠키 설정
//                .build();
//    }


//    @PostMapping("/profile_img")
//    public String ctlGetProfileImg(@RequestParam Long memberId) {
//        return memberService.getMemberProfileImage(memberId);
//    }
}

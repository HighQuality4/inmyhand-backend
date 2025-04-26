package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.common.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {
    private final RedisUtil redisUtil;
    private final RedisKeyManager redisKeyManager;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmailVerification(String email) {
        int code = new SecureRandom().nextInt(900000) + 100000; //UUID.randomUUID().toString();

        // Redis에 3분간 코드 저장
        redisUtil.set(redisKeyManager.getEmailAuthKey(code), email, 3, TimeUnit.MINUTES);

        // 메일 발송
        // String link = "http://localhost:7079/api/user/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 이메일 인증");
        message.setText("다음 코드를 입력해 인증을 완료해주세요:\n" + code);

        mailSender.send(message);
    }

    @Override
    public boolean verifyEmailCode(int code, String email) {
        String redisKey = redisKeyManager.getEmailAuthKey(code);
        String emailValue = redisUtil.get(redisKey);

        if (!email.equals(emailValue)) {
            return false; // 만료되었거나 잘못된 토큰
        }

        // 인증 완료 → redis에서 삭제하고, DB 사용자 상태 변경 등 처리
        redisUtil.delete(redisKey);

        // 이 아래에서 memberService.markEmailVerified(email); 등 호출 가능
        return true;
    }
}

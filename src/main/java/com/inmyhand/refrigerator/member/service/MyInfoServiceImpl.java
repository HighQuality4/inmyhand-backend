package com.inmyhand.refrigerator.member.service;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.member.domain.dto.ChangePwDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyInfoDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInfoServiceImpl implements MyInfoService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public Map<String, Object> findMyInfo(Long userId) {
        MemberEntity member = memberRepository.findById(userId).orElse(null);
        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("email", member.getEmail());
        memberInfo.put("nickname", member.getNickname());
        memberInfo.put("phoneNum", member.getPhoneNum());

        return memberInfo;
    }

    @Override
    @Transactional
    public boolean updateMyInfo(MyInfoDTO myInfoDTO) {
        try {
            MemberEntity member = memberRepository.findByEmail(myInfoDTO.getEmail());
            if (member == null) return false;

            member.setNickname(myInfoDTO.getNickname());
            member.setPhoneNum(myInfoDTO.getPhoneNum());
            memberRepository.save(member);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean resetPassword(String email) {
        // 1. 사용자 조회
        MemberEntity member = memberRepository.findByEmail(email);
        if (member == null) return false;

        // 2. 임시 비밀번호 생성
        String tempPassword = generateTempPassword();

        // 3. 비밀번호 암호화 후 저장
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        // 4. 이메일 발송
        sendTempPassword(email, tempPassword);

        return true;
    }

    private String generateTempPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void sendTempPassword(String email, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("임시 비밀번호 안내");
        message.setText("아래 임시 비밀번호로 로그인 후 비밀번호를 변경해주세요:\n\n" + tempPassword);

        mailSender.send(message);
    }

    public boolean changePassword(String email, String newPassword) {
        MemberEntity member = memberRepository.findByEmail(email);
        if (member != null) {
            member.setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(member);
            return true;
        } else {
            return false;
        }

    }

}

package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MyInfoDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MyInfoServiceImpl {

    private final MemberRepository memberRepository;

    public Map<String, Object> findMyInfo(Long userId) {
        MemberEntity member = memberRepository.findById(userId).orElse(null);
        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("email", member.getEmail());
        memberInfo.put("nickname", member.getNickname());
        memberInfo.put("phoneNum", member.getPhoneNum());

        return memberInfo;
    }

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
}

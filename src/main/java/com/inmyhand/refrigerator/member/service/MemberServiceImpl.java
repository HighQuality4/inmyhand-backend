package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberEntity registerLocal(MemberDTO memberDTO) {

        MemberEntity memberEntity = MemberEntity.builder()
                .memberName(memberDTO.getMemberName())
                .nickname(memberDTO.getNickname())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .email(memberDTO.getEmail())
                .phoneNum(memberDTO.getPhoneNum())
                .providerId("LOCAL")
                .status(MemberStatus.active)
                .build();

        return memberRepository.save(memberEntity);
    }

//    public String getMemberProfileImage(Long memberId) {
//        return memberRepository.findFileUrlsByMemberId(memberId);
//    }
}

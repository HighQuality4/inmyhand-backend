package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

//    public String getMemberProfileImage(Long memberId) {
//        return memberRepository.findFileUrlsByMemberId(memberId);
//    }
}

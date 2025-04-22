package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.member.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberEntity registerMember(MemberEntity memberEntity) {

        memberEntity.setPassword(passwordEncoder.encode(memberEntity.getPassword()));
        memberEntity.setProviderId("LOCAL");

        return registerRepository.save(memberEntity);
    }
}

package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;

public interface MemberService {
    boolean registerLocal(MemberDTO memberDTO);
}

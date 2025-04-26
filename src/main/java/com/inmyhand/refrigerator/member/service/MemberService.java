package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;

public interface MemberService {
    boolean registerLocal(MemberDTO memberDTO);
}

package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;

import java.util.List;

public interface MemberService {
    boolean registerLocal(MemberDTO memberDTO);
    List<MyFoodInfoDTO> MyRefreInfo(Long userId);
}

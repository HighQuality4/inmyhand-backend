package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

public interface MemberService {
    boolean registerLocal(MemberDTO memberDTO);
    List<MyFoodInfoDTO> MyRefreInfo(Long userId, Pageable pageable);
}

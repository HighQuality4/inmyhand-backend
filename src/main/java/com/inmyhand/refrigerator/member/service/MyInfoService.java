package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MyInfoDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;

import java.util.Map;

public interface MyInfoService {

    Map<String, Object> findMyInfo(Long userId);
    boolean updateMyInfo(MyInfoDTO myInfoDTO);
    boolean resetPassword(String email);
}

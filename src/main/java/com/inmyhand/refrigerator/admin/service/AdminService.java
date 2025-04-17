package com.inmyhand.refrigerator.admin.service;


import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {
    MemberEntityDto findByMemberOne(Long id);
    List<MemberEntityDto> findByMemberAll();
    void updateMember(MemberEntityDto memberEntityDto);
}

package com.inmyhand.refrigerator.admin.service;


import com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {
    MemberEntityDto findByMemberOne(Long id);
    List<MemberEntityDto> findByMemberAll();
    void updateMember(List<MemberEntityDto> memberEntityDto);
    List<MemberEntityDto> findAllMembers();
    Page<AdminRecipeInfoDto> findAllAdminRecipeInfo(Long id, String name ,Pageable pageable);
    Page<MemberEntityDto> findMemberDTOSearch(Pageable pageable, MemberCustomQueryDTO memberDTO);
}

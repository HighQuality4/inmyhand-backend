package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomQueryMemberRepository {
    Page<MemberEntityDto> searchMemberWithPaging(MemberCustomQueryDTO memberCustomQueryDTO, Pageable pageable);
}

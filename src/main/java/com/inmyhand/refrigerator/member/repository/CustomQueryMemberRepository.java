package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomQueryMemberRepository {
    List<MemberEntity> searchMember(String name, String email, String nickname);
}

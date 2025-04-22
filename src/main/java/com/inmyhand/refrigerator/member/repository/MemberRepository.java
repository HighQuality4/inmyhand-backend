package com.inmyhand.refrigerator.member.repository;


import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, CustomQueryMemberRepository {

    @Query("SELECT f.fileUrl FROM com.inmyhand.refrigerator.files.domain.entity.FilesEntity f WHERE f.memberEntity.id = :memberId")
    String findFileUrlsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.inmyhand.refrigerator.admin.dto.MemberEntityDto(" +
            "m.id, m.memberName, m.email, m.nickname, m.regdate, " +
            "m.providerId, m.status, m.phoneNum) " +
            "FROM MemberEntity m")
    List<MemberEntityDto> findAllMemberDto();

}

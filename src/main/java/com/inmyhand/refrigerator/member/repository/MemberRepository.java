package com.inmyhand.refrigerator.member.repository;


import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query("SELECT f.fileUrl FROM com.inmyhand.refrigerator.files.domain.entity.FilesEntity f WHERE f.memberEntity.id = :memberId")
    String findFileUrlsByMemberId(@Param("memberId") Long memberId);

}

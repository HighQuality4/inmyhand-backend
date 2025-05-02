package com.inmyhand.refrigerator.member.repository;


import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, CustomQueryMemberRepository {

    @Query("SELECT f.fileUrl FROM com.inmyhand.refrigerator.files.domain.entity.FilesEntity f WHERE f.memberEntity.id = :memberId")
    String findFileUrlsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.inmyhand.refrigerator.admin.dto.MemberEntityDto(" +
            "m.id, m.memberName, m.email, m.nickname, m.regdate, " +
            "m.providerId, m.status, m.phoneNum) " +
            "FROM MemberEntity m " +
            "ORDER BY m.id ASC ")
    List<MemberEntityDto> findAllMemberDto();

    @Query(value = """
    SELECT fd.food_name AS foodName, TO_CHAR(fd.end_date, 'YYYY-MM-DD') AS expdate
    FROM fridge_member fm
    JOIN fridge_food fd ON fm.fridge_id = fd.fridge_id
    WHERE fm.member_id = :memberId
    ORDER BY fd.end_date ASC
    LIMIT 5
""", nativeQuery = true)
    public List<MyFoodInfoDTO> findMyRefreInfo(@Param("memberId") Long memberId);

}

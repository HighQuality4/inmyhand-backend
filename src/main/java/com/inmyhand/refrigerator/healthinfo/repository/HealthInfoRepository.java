package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInfoRepository {

    @Query("SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, a.allergy, null, null) " +
            "FROM MemberEntity u " +
            "JOIN MemberAllergyEntity a ON u.id = a.memberEntity.id " +
            "WHERE u.id = :memberId")
    List<HealthInfoDTO> findAllergyByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, null, h.hateFood, null) " +
            "FROM MemberEntity u " +
            "JOIN HateFoodEntity h ON u.id = h.memberEntity.id " +
            "WHERE u.id = :memberId")
    List<HealthInfoDTO> findHateFoodByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, null, null, hi.interestInfo) " +
            "FROM MemberEntity u " +
            "JOIN HealthInterestEntity hi ON u.id = hi.memberEntity.id " +
            "WHERE u.id = :memberId")
    List<HealthInfoDTO> findInterestInfoByMemberId(@Param("memberId") Long memberId);

}

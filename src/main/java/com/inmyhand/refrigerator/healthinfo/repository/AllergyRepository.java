package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.entity.MemberAllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<MemberAllergyEntity, Long> {

    @Query("SELECT a.allergy FROM MemberAllergyEntity a WHERE a.memberEntity.id = :memberId")
    List<String> findAllergyByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM MemberAllergyEntity h WHERE h.memberEntity.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    List<MemberAllergyEntity> findByMemberEntityId(Long memberId);
}

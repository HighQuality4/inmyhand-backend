package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.entity.HealthInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInterestRepository extends JpaRepository<HealthInterestEntity, Long> {

    @Query("SELECT h.interestInfo FROM HealthInterestEntity h WHERE h.memberEntity.id = :memberId")
    List<String> findHealthInterestByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM HealthInterestEntity h WHERE h.memberEntity.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}

package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.entity.HateFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HateFoodRepository extends JpaRepository<HateFoodEntity, Long> {

    @Query("SELECT h.hateFood FROM HateFoodEntity h WHERE h.memberEntity.id = :memberId")
    List<String> findHateFoodByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM HateFoodEntity h WHERE h.memberEntity.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}

package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FridgeRepository extends JpaRepository<FridgeEntity, Long> {

    @Query("SELECT f FROM FridgeFoodEntity f WHERE f.fridgeEntity.id IN " +
            "(SELECT fm.fridgeEntity.id FROM FridgeMemberEntity fm WHERE fm.memberEntity.id = :memberId)")
    List<FridgeFoodEntity> findAllByMemberId(@Param("memberId") Long memberId);
}

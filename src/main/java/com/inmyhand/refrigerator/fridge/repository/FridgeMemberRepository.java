package com.inmyhand.refrigerator.fridge.repository;


import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FridgeMemberRepository extends JpaRepository<FridgeMemberEntity, Long> {

//    List<FridgeMemberEntity> findByFridgeId(Long fridgeId);
//
//    List<FridgeMemberEntity> findByMemberId(Long memberId);
//
//    FridgeMemberEntity findByMemberIdAndFridgeId(Long memberId, Long fridgeId);

}

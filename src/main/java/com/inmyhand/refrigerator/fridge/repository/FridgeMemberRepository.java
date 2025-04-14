package com.inmyhand.refrigerator.fridge.repository;


import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FridgeMemberRepository extends JpaRepository<FridgeMemberEntity, Long> {

    @EntityGraph(attributePaths = {
            "memberEntity",
            "memberGroupRoleEntities",
            "memberGroupRoleEntities.groupRoleEntity"
    })
    List<FridgeMemberEntity> findByFridgeEntity_Id(Long fridgeId);

    Optional<FridgeMemberEntity> findByFridgeEntity_IdAndMemberEntity_Id(Long fridgeId, Long memberId);
    Optional<FridgeMemberEntity> findByFridgeEntity_IdAndMemberEntity_Nickname(Long fridgeId, String nickname);
    List<FridgeMemberEntity> findByMemberEntity_Id(Long memberId);

}

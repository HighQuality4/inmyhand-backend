package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeLeaderDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.MemberGroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.security.Permissions;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberGroupRoleRepository extends JpaRepository<MemberGroupRoleEntity, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberGroupRoleEntity m WHERE m.fridgeMemberEntity.id = :fridgeMemberId")
    void deleteByFridgeMemberId(@Param("fridgeMemberId") Long fridgeMemberId);

    @Query("""
    SELECT new com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeLeaderDTO(
        f.id,
        f.fridgeName,
        fm.joinDate
    )
    FROM MemberGroupRoleEntity mgr
    JOIN mgr.fridgeMemberEntity fm
    JOIN fm.fridgeEntity f
    JOIN mgr.groupRoleEntity gr
    WHERE fm.memberEntity.id = :memberId
    AND gr.roleName = 'leader'
    """)
    List<FridgeLeaderDTO> findFridgeWhereUserIsLeader(@Param("memberId") Long memberId);


    /**
     * 주어진 냉장고(fridgeId)에 속한 memberId의 roleName 리스트를 가져옵니다.
     */
    @Query("""
      SELECT mgr.groupRoleEntity.roleName
      FROM MemberGroupRoleEntity mgr
      WHERE mgr.fridgeMemberEntity.fridgeEntity.id = :fridgeId
        AND mgr.fridgeMemberEntity.memberEntity.id = :memberId
    """)
    List<String> findRoleNamesByFridgeAndMember(
            @Param("fridgeId") Long fridgeId,
            @Param("memberId") Long memberId
    );
}

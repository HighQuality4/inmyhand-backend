package com.inmyhand.refrigerator.fridge.repository;


import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeMemberPendingDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FridgeMemberRepository extends JpaRepository<FridgeMemberEntity, Long> {

    @Query("SELECT fm.fridgeEntity FROM FridgeMemberEntity fm WHERE fm.memberEntity.id = :memberId")
    List<FridgeEntity> findFridgesByMemberId(@Param("memberId") Long memberId);


    @Query("SELECT fm FROM FridgeMemberEntity fm " +
            "LEFT JOIN FETCH fm.permissionGroupRoleList pgr " +
            "LEFT JOIN FETCH pgr.groupRoleEntity " +
            "WHERE fm.fridgeEntity.id = :fridgeId")
    List<FridgeMemberEntity> findFridgeMembersWithRolesByFridgeId(@Param("fridgeId") Long fridgeId);


    @Query(value = """
    SELECT 
        fm.fridge_id AS fridgeId,
        f.fridge_name AS fridgeName,
        STRING_AGG(gr.role_name, ',' ORDER BY gr.role_name) AS roleNames,
        fm.favorite_state AS favoriteState
    FROM fridge_member fm
    LEFT JOIN fridge f ON fm.fridge_id = f.fridge_id
    LEFT JOIN member_group_role mgr ON fm.fridge_member_id = mgr.fridge_member_id
    LEFT JOIN group_role gr ON mgr.group_role_id = gr.group_role_id
    WHERE fm.member_id = :memberId
      AND fm.state = true  
    GROUP BY fm.fridge_member_id, f.fridge_name, fm.favorite_state, fm.fridge_id
    """,
            nativeQuery = true)
    List<Object[]> findFridgeListWithRolesNative(@Param("memberId") Long memberId);

    /*@Query("SELECT fm FROM FridgeMemberEntity fm " +
            "JOIN FETCH fm.fridgeEntity f " +  // 냉장고 정보까지 가져옴
            "WHERE fm.memberEntity.id = :memberId " +
            "AND fm.state = false")
    List<FridgeMemberEntity> findPendingInvitesWithFridgeByMemberId(@Param("memberId") Long memberId);*/

    @Query("""
    SELECT new com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeMemberPendingDTO(
        f.id,
        f.fridgeName,
        fm.joinDate,
        fm.state,
        fm.favoriteState
    )
    FROM FridgeMemberEntity fm
    JOIN fm.fridgeEntity f
    WHERE fm.memberEntity.id = :memberId
    AND fm.state = false
    ORDER BY fm.joinDate DESC
    """)
    List<FridgeMemberPendingDTO> findPendingFridgeInvitesDtoByMemberId(@Param("memberId") Long memberId);


    Optional<FridgeMemberEntity> findByFridgeEntity_IdAndMemberEntity_Id(Long fridgeId, Long memberId);


    List<FridgeMemberEntity> findByMemberEntity_Id(Long memberId);

    // 즐겨찾기 냉장고 전체 조회
    List<FridgeMemberEntity> findByMemberEntity_IdAndFavoriteStateTrue(Long memberId);

    // 즐겨찾기 냉장고 중 가장 먼저 가입한 냉장고 (메인)
    Optional<FridgeMemberEntity> findFirstByMemberEntity_IdAndFavoriteStateTrueOrderByJoinDateAsc(Long memberId);
}

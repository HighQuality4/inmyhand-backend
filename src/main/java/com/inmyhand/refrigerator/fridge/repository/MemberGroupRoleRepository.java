package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.FridgeMemberEntity;
import com.inmyhand.refrigerator.fridge.domain.entity.MemberGroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permissions;
import java.util.List;

@Repository
public interface MemberGroupRoleRepository extends JpaRepository<MemberGroupRoleEntity, Long> {
    List<MemberGroupRoleEntity> findByFridgeMemberEntity_Id(Long id);
    List<MemberGroupRoleEntity> findAllByFridgeMemberEntity_Id(Long fridgeMemberId);
//    MemberGroupRoleEntity findByFridgeMemberIdAndGroupRoleId(Long fridgeMemberId, Long groupRoleId);
    List<MemberGroupRoleEntity> findByFridgeMemberEntity(FridgeMemberEntity fridgeMember);


}

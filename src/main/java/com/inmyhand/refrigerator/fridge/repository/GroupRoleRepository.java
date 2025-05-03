package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.dto.food.RoleCheckboxDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.GroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.security.Permissions;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRoleEntity, Long> {


    @Query("SELECT new com.inmyhand.refrigerator.fridge.domain.dto.food.RoleCheckboxDTO(" +
            "gr.id, gr.roleName, gr.roleDescription) " +
            "FROM GroupRoleEntity gr")
    List<RoleCheckboxDTO> findAllRoleCheckboxDTO();


    // 역할 이름으로 group_role 엔티티 찾기
    Optional<GroupRoleEntity> findByRoleNameIgnoreCase(String roleName);
}

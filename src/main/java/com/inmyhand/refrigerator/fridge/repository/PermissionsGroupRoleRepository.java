package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.PermissionGroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permissions;
import java.util.List;

@Repository
public interface PermissionsGroupRoleRepository extends JpaRepository<PermissionGroupRoleEntity, Long> {
//    List<PermissionGroupRoleEntity> findByGroupRoleId(Long groupRoleId);
}

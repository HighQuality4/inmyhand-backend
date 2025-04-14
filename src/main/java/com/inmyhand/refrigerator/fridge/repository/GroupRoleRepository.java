package com.inmyhand.refrigerator.fridge.repository;

import com.inmyhand.refrigerator.fridge.domain.entity.GroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permissions;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRoleEntity, Long> {

}

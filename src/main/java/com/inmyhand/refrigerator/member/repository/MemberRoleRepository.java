package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.member.domain.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {

}

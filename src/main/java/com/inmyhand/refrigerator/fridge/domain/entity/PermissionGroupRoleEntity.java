package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "permission_group_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"groupRoleEntity", "permissionEntity"})
public class PermissionGroupRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_role_id")
    private Long id;

    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_role_id")
    @JsonIgnoreProperties("permissionGroupRoleList")
    private GroupRoleEntity groupRoleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    @JsonIgnoreProperties("permissionsGroupRoleList")
    private PermissionEntity permissionEntity;
}

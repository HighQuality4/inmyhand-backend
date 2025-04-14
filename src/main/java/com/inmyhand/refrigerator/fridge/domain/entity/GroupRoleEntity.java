package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"permissionGroupRoleList", "userGroupRoleList"})
public class GroupRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @OneToMany(mappedBy = "groupRoleEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("groupRoleEntity")
    private List<PermissionGroupRoleEntity> permissionGroupRoleList = new ArrayList<>();

    @OneToMany(mappedBy = "groupRoleEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("groupRoleEntity")
    private List<MemberGroupRoleEntity> userGroupRoleList = new ArrayList<>();
}

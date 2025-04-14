package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"permissionsGroupRoleList"})
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;

    @OneToMany(mappedBy = "permissionEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("permissionEntity")
    private List<PermissionGroupRoleEntity> permissionsGroupRoleList = new ArrayList<>();




    // 연관관계 제거 메서드 추가
    public void removePermissionGroupRole(PermissionGroupRoleEntity permissionGroupRole) {
        this.permissionsGroupRoleList.remove(permissionGroupRole);
        permissionGroupRole.setPermissionEntity(null);
    }

    // 모든 연관관계 제거 메서드
    public void clearPermissionGroupRoles() {
        List<PermissionGroupRoleEntity> groupRolesToRemove = new ArrayList<>(this.permissionsGroupRoleList);

        for (PermissionGroupRoleEntity groupRole : groupRolesToRemove) {
            removePermissionGroupRole(groupRole);
        }

        this.permissionsGroupRoleList.clear();
    }
}

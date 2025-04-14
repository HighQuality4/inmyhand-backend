package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "member_group_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"fridgeMemberEntity", "groupRoleEntity"})
public class MemberGroupRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_group_role_id")
    private Long id;

    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_member_id")
    @JsonIgnoreProperties("permissionGroupRoleList")
    private FridgeMemberEntity fridgeMemberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_role_id")
    @JsonIgnoreProperties("permissionGroupRoleList")
    private GroupRoleEntity groupRoleEntity;
}

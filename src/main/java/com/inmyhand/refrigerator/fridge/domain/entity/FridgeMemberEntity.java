package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.*;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "fridge_member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity", "fridgeEntity"})
public class FridgeMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_member_id")
    private Long id;

    private Date joinDate;

    private Boolean state;

    private Boolean favoriteState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nickname")
    @JsonIdentityReference(alwaysAsId = true)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private FridgeEntity fridgeEntity;

    @OneToMany(mappedBy = "fridgeMemberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("fridgeMemberEntity")
    @JsonIgnore
    @BatchSize(size = 10)
    private List<MemberGroupRoleEntity> permissionGroupRoleList;

    /**
     * 멤버 설정 메서드 (양방향 연관관계 처리)
     * @param member 설정할 멤버
     */
    public void setMember(MemberEntity member) {
        // 기존 멤버와의 연관관계 제거
        if (this.memberEntity != null && this.memberEntity != member) {
            this.memberEntity.getFridgeMemberList().remove(this);
        }

        this.memberEntity = member;

        // 새 멤버와 양방향 연관관계 설정
        if (member != null && !member.getFridgeMemberList().contains(this)) {
            member.getFridgeMemberList().add(this);
        }
    }

    /**
     * 냉장고 설정 메서드 (양방향 연관관계 처리)
     * @param fridge 설정할 냉장고
     */
    public void setFridge(FridgeEntity fridge) {
        // 기존 냉장고와의 연관관계 제거
        if (this.fridgeEntity != null && this.fridgeEntity != fridge) {
            this.fridgeEntity.getFridgeMemberList().remove(this);
        }

        this.fridgeEntity = fridge;

        // 새 냉장고와 양방향 연관관계 설정
        if (fridge != null && !fridge.getFridgeMemberList().contains(this)) {
            fridge.getFridgeMemberList().add(this);
        }
    }

    /**
     * 멤버와 냉장고 연관관계 모두 제거
     */
    public void removeAllRelationships() {
        if (this.memberEntity != null) {
            this.memberEntity.getFridgeMemberList().remove(this);
            this.memberEntity = null;
        }

        if (this.fridgeEntity != null) {
            this.fridgeEntity.getFridgeMemberList().remove(this);
            this.fridgeEntity = null;
        }
    }
}

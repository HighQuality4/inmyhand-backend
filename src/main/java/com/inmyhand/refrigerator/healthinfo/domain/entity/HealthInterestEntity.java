package com.inmyhand.refrigerator.healthinfo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "health_interest",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "health_interest_category_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity", "healthInterestCategoryEntity"})
public class HealthInterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @Column(name = "interest_info", length = 100)
    private String interestInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties("healthInterestList")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_interest_category_id")
    @JsonIgnoreProperties("healthInterestCategoryEntityList")
    private HealthInterestCategoryEntity healthInterestCategoryEntity;

    /**
     * 회원 설정 메서드 (양방향 연관관계 처리)
     * @param member 설정할 회원 엔티티
     */
    public void setMember(MemberEntity member) {
        // 기존 회원과의 연관관계 제거
        if (this.memberEntity != null && this.memberEntity != member) {
            this.memberEntity.getHealthInterestList().remove(this);
        }

        this.memberEntity = member;

        // 새 회원과 양방향 연관관계 설정
        if (member != null && !member.getHealthInterestList().contains(this)) {
            member.getHealthInterestList().add(this);
        }
    }

    /**
     * 건강 관심사 카테고리 설정 메서드 (양방향 연관관계 처리)
     * @param category 설정할 카테고리 엔티티
     */
    public void setHealthInterestCategory(HealthInterestCategoryEntity category) {
        // 기존 카테고리와의 연관관계 제거
        if (this.healthInterestCategoryEntity != null && this.healthInterestCategoryEntity != category) {
            // 필드명은 실제 HealthInterestCategoryEntity에 정의된 이름과 일치해야 함
            this.healthInterestCategoryEntity.getHealthInterestEntities().remove(this);
        }

        this.healthInterestCategoryEntity = category;

        // 새 카테고리와 양방향 연관관계 설정
        if (category != null && !category.getHealthInterestEntities().contains(this)) {
            category.getHealthInterestEntities().add(this);
        }
    }

    /**
     * 회원 연관관계 제거 메서드
     */
    public void removeMember() {
        if (this.memberEntity != null) {
            this.memberEntity.getHealthInterestList().remove(this);
            this.memberEntity = null;
        }
    }

    /**
     * 카테고리 연관관계 제거 메서드
     */
    public void removeHealthInterestCategory() {
        if (this.healthInterestCategoryEntity != null) {
            this.healthInterestCategoryEntity.getHealthInterestEntities().remove(this);
            this.healthInterestCategoryEntity = null;
        }
    }

    /**
     * 모든 연관관계 제거 메서드
     */
    public void removeAllRelationships() {
        removeMember();
        removeHealthInterestCategory();
    }

}



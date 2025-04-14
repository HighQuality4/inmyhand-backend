package com.inmyhand.refrigerator.healthinfo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.recipe.domain.entity.AnalysisHealthInterestEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "health_interest_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HealthInterestCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_interest_category_id")
    private Long id;

    @Column(name = "health_interest_category_name", length = 100)
    private String healthInterestCategoryName;

    @OneToMany(mappedBy = "healthInterestCategoryEntity")
    @JsonIgnoreProperties("healthInterestCategoryEntity")
    private List<HealthInterestEntity> healthInterestEntities = new ArrayList<>();

    @OneToMany(mappedBy = "healthInterestCategoryEntity")
    @JsonIgnoreProperties("healthInterestCategoryEntity")
    private List<AnalysisHealthInterestEntity> analysisHealthInterestList = new ArrayList<>();

    /**
     * HealthInterestEntity와의 연관관계를 제거하는 메서드
     */
    public void removeHealthInterests() {
        if (this.healthInterestEntities == null) {
            return;
        }

        // ConcurrentModificationException 방지를 위해 새 리스트로 복사
        List<HealthInterestEntity> interestsToRemove = new ArrayList<>(this.healthInterestEntities);

        for (HealthInterestEntity interest : interestsToRemove) {
            // 양방향 연관관계 제거
            interest.setHealthInterestCategoryEntity(null);
        }

        this.healthInterestEntities.clear();
    }

    /**
     * AnalysisHealthInterestEntity와의 연관관계를 제거하는 메서드
     */
    public void removeAnalysisHealthInterests() {
        if (this.analysisHealthInterestList == null) {
            return;
        }

        // ConcurrentModificationException 방지를 위해 새 리스트로 복사
        List<AnalysisHealthInterestEntity> analysisToRemove =
                new ArrayList<>(this.analysisHealthInterestList);

        for (AnalysisHealthInterestEntity analysis : analysisToRemove) {
            // 양방향 연관관계 제거
            analysis.setHealthInterestCategoryEntity(null);
        }

        this.analysisHealthInterestList.clear();
    }

    /**
     * 모든 연관관계를 한 번에 제거하는 메서드
     * 엔티티 삭제 전에 호출해야 함
     */
    public void removeAllRelationships() {
        removeHealthInterests();
        removeAnalysisHealthInterests();
    }

    /**
     * HealthInterestEntity 추가 메서드
     */
    public void addHealthInterest(HealthInterestEntity interest) {
        if (interest == null) return;

        if (this.healthInterestEntities == null) {
            this.healthInterestEntities = new ArrayList<>();
        }

        if (!this.healthInterestEntities.contains(interest)) {
            this.healthInterestEntities.add(interest);
            if (interest.getHealthInterestCategoryEntity() != this) {
                interest.setHealthInterestCategoryEntity(this);
            }
        }
    }

    /**
     * AnalysisHealthInterestEntity 추가 메서드
     */
    public void addAnalysisHealthInterest(AnalysisHealthInterestEntity analysis) {
        if (analysis == null) return;

        if (this.analysisHealthInterestList == null) {
            this.analysisHealthInterestList = new ArrayList<>();
        }

        if (!this.analysisHealthInterestList.contains(analysis)) {
            this.analysisHealthInterestList.add(analysis);
            if (analysis.getHealthInterestCategoryEntity() != this) {
                analysis.setHealthInterestCategoryEntity(this);
            }
        }
    }
}
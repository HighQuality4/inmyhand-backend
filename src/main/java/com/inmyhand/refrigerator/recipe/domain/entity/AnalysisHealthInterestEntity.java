package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HealthInterestCategoryEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "analysis_health_interest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recipeNutrientAnalysisEntity", "healthInterestCategoryEntity"})
public class AnalysisHealthInterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_health_id")
    private Long id;

    @Column(name = "created_at", columnDefinition = "timestamp default now()", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_analysis_id", nullable = false)
//    @JsonIgnoreProperties("analysisHealthInterestList")
    @JsonIgnore
    private RecipeNutrientAnalysisEntity recipeNutrientAnalysisEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_Interest_category_id")
//    @JsonIgnoreProperties("analysisHealthInterestList")
    @JsonIgnore
    private HealthInterestCategoryEntity healthInterestCategoryEntity;
}

package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe_nutrient_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recipeInfoEntity", "analysisHealthInterestList"})
public class RecipeNutrientAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrient_analysis_id")
    private Long id;

    @Column(name = "analysis_result", nullable = false)
    private String analysisResult;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "carbs", nullable = false)
    private int carbs;

    @Column(name = "protein", nullable = false)
    private int protein;

    @Column(name = "fat", nullable = false)
    private int fat;

    @Column(name = "mineral", nullable = false)
    private int mineral;

    @Column(name = "vitamin", nullable = false)
    private int vitamin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnoreProperties("recipeNutrientAnalysisList")
    private RecipeInfoEntity recipeInfoEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties("recipeNutrientAnalysisList")
    private MemberEntity memberEntity;
}

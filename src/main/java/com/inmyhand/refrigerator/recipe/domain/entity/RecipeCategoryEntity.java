package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.recipe.domain.enums.CategoryTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recipeInfoEntity"})
public class RecipeCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_category_id")
    private Long id;

    @Column(name = "recipe_category_name", nullable = false)
    private String recipeCategoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_category_type", nullable = false)
    private CategoryTypeEnum recipeCategoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
//    @JsonIgnoreProperties("recipeCategoryList")
    @JsonIgnore
    private RecipeInfoEntity recipeInfoEntity;
}

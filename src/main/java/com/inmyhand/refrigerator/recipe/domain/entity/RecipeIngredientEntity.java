package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recipeInfoEntity"})
@Builder
public class RecipeIngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(name = "ingredient_group")
    private String ingredientGroup;

    @Column(name = "ingredient_quantity", nullable = false)
    private double ingredientQuantity;

    @Column(name = "ingredient_unit", nullable = false)
    private String ingredientUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private RecipeInfoEntity recipeInfoEntity;

    public RecipeIngredientEntity(String ingredientName, Integer ingredientQuantity,  String ingredientUnit, RecipeInfoEntity recipeInfoEntity) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientUnit = ingredientUnit;
        this.recipeInfoEntity = recipeInfoEntity;
    }

    /**
     * 레시피 정보 엔티티와의 양방향 연관관계를 관리하는 편의 메서드
     * @param recipeInfoEntity 설정할 RecipeInfoEntity 객체
     */
    public void setRecipeInfoEntity(RecipeInfoEntity recipeInfoEntity) {
        // 기존에 연결된 레시피가 있다면, 그 레시피의 재료 목록에서 현재 재료 제거
        if (this.recipeInfoEntity != null) {
            this.recipeInfoEntity.getRecipeIngredientList().remove(this);
        }

        // 새로운 레시피 설정
        this.recipeInfoEntity = recipeInfoEntity;

        // 새로운 레시피가 null이 아니고, 그 레시피의 재료 목록에 현재 재료가 없다면 추가
        if (recipeInfoEntity != null && !recipeInfoEntity.getRecipeIngredientList().contains(this)) {
            recipeInfoEntity.getRecipeIngredientList().add(this);
        }
    }
}

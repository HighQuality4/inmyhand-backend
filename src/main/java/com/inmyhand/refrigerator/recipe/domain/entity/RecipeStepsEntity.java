package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recipeInfoEntity"})
public class RecipeStepsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long id;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(name = "step_description", nullable = false)
    private String stepDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnoreProperties("recipeIngredientList")
    private RecipeInfoEntity recipeInfoEntity;

    @OneToOne(mappedBy = "recipeStepEntity", cascade = CascadeType.ALL)
    private FilesEntity filesEntity;

    /**
     * 레시피 정보 엔티티와의 양방향 연관관계를 관리하는 편의 메서드
     * @param recipeInfoEntity 설정할 RecipeInfoEntity 객체
     */
    public void setRecipeInfoEntity(RecipeInfoEntity recipeInfoEntity) {
        // 기존에 연결된 레시피가 있다면, 그 레시피의 단계 목록에서 현재 단계 제거
        if (this.recipeInfoEntity != null) {
            this.recipeInfoEntity.getRecipeStepsList().remove(this);
        }

        // 새로운 레시피 설정
        this.recipeInfoEntity = recipeInfoEntity;

        // 새로운 레시피가 null이 아니고, 그 레시피의 단계 목록에 현재 단계가 없다면 추가
        if (recipeInfoEntity != null && !recipeInfoEntity.getRecipeStepsList().contains(this)) {
            recipeInfoEntity.getRecipeStepsList().add(this);
        }
    }

}

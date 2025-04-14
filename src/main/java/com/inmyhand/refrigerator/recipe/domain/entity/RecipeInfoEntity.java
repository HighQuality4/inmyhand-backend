package com.inmyhand.refrigerator.recipe.domain.entity;

import com.fasterxml.jackson.annotation.*;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.recipe.domain.enums.CookingTimeEnum;
import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recipe_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberEntity", "parentRecipe", "childRecipes", "recipeStepsList", "recipeIngredientList", "recipeCommentList", "recipeLikesList", "recipeCategoryList", "recipeViewsList", "recipeNutrientAnalysisList", "filesList"})
public class RecipeInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private DifficultyEnum difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "cooking_time")
    private CookingTimeEnum cookingTime;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "summary")
    private String summary;

    @Column(name = "servings", nullable = false)
    private Integer servings;

    @Column(name = "recipe_depth", nullable = false)
    private Integer recipeDepth = 0; // 기본 값을 0으로 지정

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties("recipeInfoList")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_recipe_id")
    @JsonBackReference
    private RecipeInfoEntity parentRecipe;

    @OneToMany(mappedBy = "parentRecipe", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<RecipeInfoEntity> childRecipes = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeStepsEntity> recipeStepsList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeIngredientEntity> recipeIngredientList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeCategoryEntity> recipeCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<FilesEntity> filesEntities = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeCommentEntity> recipeCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeLikesEntity> recipeLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeViewsEntity> recipeViewsList = new ArrayList<>();

    @OneToMany(mappedBy = "recipeInfoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeInfoEntity")
    private List<RecipeNutrientAnalysisEntity> recipeNutrientAnalysisList = new ArrayList<>();


    /**
     * 자식 레시피 추가
     * 양방향 관계 모두 설정
     */
    public void addChildRecipe(RecipeInfoEntity childRecipe) {
        if (childRecipe == null) {
            return;
        }

        if (!this.childRecipes.contains(childRecipe)) {
            this.childRecipes.add(childRecipe);
        }

        // 양방향 관계 설정
        if (childRecipe.getParentRecipe() != this) {
            childRecipe.setParentRecipe(this);
        }
    }

    /**
     * 자식 레시피 제거
     * 양방향 관계 모두 해제
     */
    public void removeChildRecipe(RecipeInfoEntity childRecipe) {
        if (childRecipe == null) {
            return;
        }

        this.childRecipes.remove(childRecipe);

        // 양방향 관계 해제
        if (childRecipe.getParentRecipe() == this) {
            childRecipe.setParentRecipe(null);
        }
    }

    /**
     * 모든 자식 레시피와의 연관관계 제거
     */
    public void clearChildRecipes() {
        // ConcurrentModificationException 방지를 위해 새 리스트 생성
        for (RecipeInfoEntity childRecipe : new ArrayList<>(this.childRecipes)) {
            removeChildRecipe(childRecipe);
        }
    }

    /**
     * 부모 레시피 설정
     * 기존 부모와의 관계 해제 및 새 부모와의 관계 설정
     */
    public void setParentRecipe(RecipeInfoEntity parentRecipe) {
        // 현재 부모와 새 부모가 같으면 아무 작업 안함
        if (this.parentRecipe == parentRecipe) {
            return;
        }

        // 기존 부모와의 관계 해제
        if (this.parentRecipe != null) {
            this.parentRecipe.getChildRecipes().remove(this);
        }

        // 새 부모 설정
        this.parentRecipe = parentRecipe;

        // 새 부모의 자식 리스트에 현재 레시피 추가
        if (parentRecipe != null && !parentRecipe.getChildRecipes().contains(this)) {
            parentRecipe.getChildRecipes().add(this);
        }
    }
    /*

     // 부모 레시피와의 연관관계 제거

        // 예시 1: 자식 레시피 추가
        RecipeInfoEntity parentRecipe = recipeRepository.findById(parentId).orElseThrow();
        RecipeInfoEntity childRecipe = new RecipeInfoEntity();
        // childRecipe 필드 설정...

        parentRecipe.addChildRecipe(childRecipe);
        recipeRepository.save(parentRecipe); // cascade 설정에 따라 childRecipe도 저장됨

        // 예시 2: 레시피의 부모 변경
        RecipeInfoEntity recipe = recipeRepository.findById(recipeId).orElseThrow();
        RecipeInfoEntity newParent = recipeRepository.findById(newParentId).orElseThrow();

        recipe.setParentRecipe(newParent);
        recipeRepository.save(recipe);

        // 예시 3: 부모와의 관계 제거 (독립 레시피로 만들기)
        recipe.detachFromParent();
        recipeRepository.save(recipe);


    */
}

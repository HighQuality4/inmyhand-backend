package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "fridge_food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"fridgeEntity"})
public class FridgeFoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_food_id")
    private Long id;

    private String foodName;
    private Long foodAmount;
    private Date endDate;
    private Date chargeDate;
    private Date saveDate;
    private String categoryName;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fridge_id")
    @JsonIgnoreProperties("fridgeFoodList")
    private FridgeEntity fridgeEntity;


    /**
     * 냉장고 설정 메서드 (양방향 연관관계 처리)
     *
     * @param fridge 설정할 냉장고
     */
    public void setFridge(FridgeEntity fridge) {
        // 기존 냉장고와의 연관관계 제거
        if (this.fridgeEntity != null && this.fridgeEntity != fridge) {
            this.fridgeEntity.getFridgeFoodList().remove(this);
        }

        this.fridgeEntity = fridge;

        // 새 냉장고와 양방향 연관관계 설정
        if (fridge != null && !fridge.getFridgeFoodList().contains(this)) {
            fridge.getFridgeFoodList().add(this);
        }
    }

    /**
     * 냉장고 연관관계 제거 메서드
     */
    public void removeFridge() {
        if (this.fridgeEntity != null) {
            this.fridgeEntity.getFridgeFoodList().remove(this);
            this.fridgeEntity = null;
        }
    }


// 사용 예시 ------------------------------------------------
    /*
 // 카테고리 설정
FridgeFoodEntity food = new FridgeFoodEntity();
FoodCategoryEntity category = categoryRepository.findById(1L).orElseThrow();
food.setFoodCategory(category);

// 카테고리 변경
FoodCategoryEntity newCategory = categoryRepository.findById(2L).orElseThrow();
food.setFoodCategory(newCategory);

// 카테고리 제거
food.removeFoodCategory();

    // 냉장고 설정
FridgeEntity fridge = fridgeRepository.findById(1L).orElseThrow();
food.setFridge(fridge);

// 냉장고 변경
FridgeEntity newFridge = fridgeRepository.findById(2L).orElseThrow();
food.setFridge(newFridge);

// 냉장고 연관관계 제거
food.removeFridge();

    * ------------------------------------------------
    */
}

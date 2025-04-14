package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "food_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"fridgeFoodList"})
public class FoodCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_category_id")
    private Long id;

    private String categoryName;
    private Long endInfo;

    @OneToMany(mappedBy = "foodCategoryEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("foodCategoryEntity")
    private List<FridgeFoodEntity> fridgeFoodList = new ArrayList<>();

    /**
     * 냉장고 음식 추가 메서드
     * @param fridgeFood 추가할 냉장고 음식 엔티티
     */
    public void addFridgeFood(FridgeFoodEntity fridgeFood) {
        // NullPointerException 방지
        if (this.fridgeFoodList == null) {
            this.fridgeFoodList = new ArrayList<>();
        }

        // 이미 존재하는지 확인하여 중복 방지
        if (!this.fridgeFoodList.contains(fridgeFood)) {
            this.fridgeFoodList.add(fridgeFood);

            // 양방향 연관관계 설정
            if (fridgeFood.getFoodCategoryEntity() != this) {
                fridgeFood.setFoodCategoryEntity(this);
            }
        }
    }

    /**
     * 냉장고 음식 제거 메서드
     * @param fridgeFood 제거할 냉장고 음식 엔티티
     */
    public void removeFridgeFood(FridgeFoodEntity fridgeFood) {
        if (this.fridgeFoodList != null) {
            this.fridgeFoodList.remove(fridgeFood);

            // 양방향 연관관계 제거
            if (fridgeFood.getFoodCategoryEntity() == this) {
                fridgeFood.setFoodCategoryEntity(null);
            }
        }
    }

    /**
     * 특정 냉장고 음식을 ID로 찾아 제거하는 메서드
     * @param fridgeFoodId 제거할 냉장고 음식 ID
     * @return 제거 성공 여부
     */
    public boolean removeFridgeFoodById(Long fridgeFoodId) {
        if (this.fridgeFoodList != null) {
            for (Iterator<FridgeFoodEntity> iterator = this.fridgeFoodList.iterator(); iterator.hasNext();) {
                FridgeFoodEntity fridgeFood = iterator.next();
                if (fridgeFood.getId() != null && fridgeFood.getId().equals(fridgeFoodId)) {
                    iterator.remove();
                    fridgeFood.setFoodCategoryEntity(null);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 모든 냉장고 음식 연관관계 제거 메서드
     */
    public void clearFridgeFoods() {
        if (this.fridgeFoodList != null) {
            // ConcurrentModificationException 방지를 위해 새 리스트로 복사
            List<FridgeFoodEntity> foods = new ArrayList<>(this.fridgeFoodList);

            for (FridgeFoodEntity food : foods) {
                removeFridgeFood(food);
            }

            this.fridgeFoodList.clear();
        }
    }
}

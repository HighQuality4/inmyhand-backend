package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategoryDTO {
    private Long id;
    private String categoryName;
    private Long endInfo;
}

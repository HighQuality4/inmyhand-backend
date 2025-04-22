package com.inmyhand.refrigerator.category.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchFoodCategoryDTO {

    private Long categoryId;        // 사용자가 넣은 값
    private String categoryName;    // 유사한 category
    private int expirationInfo;     // 유통기한

}


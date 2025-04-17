package com.inmyhand.refrigerator.category.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodVectorRequestDTO {
    private String inputText;        // 사용자가 넣은 값
    private String categoryName;    // 유사한 category
    private String naturalText;     // 원래 DB text
    private int expirationInfo;     // 유통기한
    private Double distance;        // 유사도
}


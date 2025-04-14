package com.inmyhand.refrigerator.category.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GptResponseDTO {
    private String category;
    private int expirationInfo;
}

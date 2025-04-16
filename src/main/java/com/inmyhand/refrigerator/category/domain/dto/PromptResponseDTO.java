package com.inmyhand.refrigerator.category.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PromptResponseDTO {
    private String category;
    private int expirationInfo;
    private String naturalText;
}
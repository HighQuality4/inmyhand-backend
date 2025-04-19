package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FridgeWithRolesDTO {
    private Long fridgeId;
    private String fridgeName;
    private List<String> roleNames;  // 역할 여러 개
    private Boolean favoriteState;
}

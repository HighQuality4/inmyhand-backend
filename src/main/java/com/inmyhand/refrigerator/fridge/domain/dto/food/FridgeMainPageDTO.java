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
public class FridgeMainPageDTO {

    private Long selectFridgeId;                      // 메인 냉장고 ID

    private List<FridgeFoodDTO> foodList;           // 메인 냉장고의 식재료 목록
}

package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeDTO {

    private String fridgeName;
    private Long fridgeId;
}

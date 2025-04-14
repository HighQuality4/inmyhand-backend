package com.inmyhand.refrigerator.fridge.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeDTO {
    private Long id;
    private String fridgeName;
    private Boolean favoriteState;
}

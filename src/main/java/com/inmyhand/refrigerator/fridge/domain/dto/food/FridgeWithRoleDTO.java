package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeWithRoleDTO {
    private Long fridgeId;
    private String fridgeName;
    private Boolean favoriteState;
    private Boolean isLeader;


}

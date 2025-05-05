package com.inmyhand.refrigerator.fridge.domain.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptInviteRequestDTO {
    private Long memberId;
    private Long fridgeId;
    // getters/setters, lombok 어노테이션 등
}
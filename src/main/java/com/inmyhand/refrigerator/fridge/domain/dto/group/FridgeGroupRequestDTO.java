package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FridgeGroupRequestDTO {
    private Long memberId;
    private Date joinDate;

}
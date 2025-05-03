package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FridgeMemberPendingDTO {
    private Long fridgeId;
    private String fridgeName;
    private Date joinDate;  // 초대 받은 날짜
    private Boolean state;
    private Boolean favoriteState;
}

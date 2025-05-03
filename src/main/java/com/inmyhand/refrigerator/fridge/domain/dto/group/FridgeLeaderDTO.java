package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FridgeLeaderDTO {
    private Long fridgeId;
    private String fridgeName;
    private Date joinDate;
}

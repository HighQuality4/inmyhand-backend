package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FridgeGroupEditDTO {
    private Long fridgeMemberId;
    private Long memberId;
    private Date joinDate;
    private String permissionName;
    private String roleStatus;
}

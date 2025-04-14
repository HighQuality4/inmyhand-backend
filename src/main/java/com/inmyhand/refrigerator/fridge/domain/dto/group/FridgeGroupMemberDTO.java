package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FridgeGroupMemberDTO {
    private Long fridgeMemberId;
    private Long memberId;
    private String nickname;
    private String email;
    private Date joinDate;
    private String roleName; // 예: "OWNER", "MEMBER"
}

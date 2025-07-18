package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FridgeGroupMemberDTO {
    private Long fridgeMemberId;
    private Long memberId;
    private String nickname;
    private String email;
    private Date joinDate;
    private List<String> roleName; // 예: "leader", "member", editor
}

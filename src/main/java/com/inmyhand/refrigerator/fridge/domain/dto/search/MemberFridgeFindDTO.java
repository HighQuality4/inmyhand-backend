package com.inmyhand.refrigerator.fridge.domain.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFridgeFindDTO {
    private Long   memberId;
    private String email;
    private String memberName;
    private String nickname;
}

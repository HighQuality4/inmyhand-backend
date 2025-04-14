package com.inmyhand.refrigerator.fridge.domain.dto.group;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGroupRoleDTO {
    private Long id;
    private Timestamp startDate;
    private Long fridgeMembersId;
    private Long groupRoleId;
}

package com.inmyhand.refrigerator.fridge.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRoleDTO {
    private Long id;
    private String roleName;
    private String roleDescription;
}

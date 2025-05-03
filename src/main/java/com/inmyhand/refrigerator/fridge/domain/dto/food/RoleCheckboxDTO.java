package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleCheckboxDTO {
    private Long roleId;
    private String roleName;         // ex) writer
    private String roleDescription;  // ex) 작성자
}

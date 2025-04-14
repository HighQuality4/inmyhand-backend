package com.inmyhand.refrigerator.fridge.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionsDTO {
    private Long id;
    private String permissionsName;
    private String permissionsDescription;
}

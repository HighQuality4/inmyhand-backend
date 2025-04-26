package com.inmyhand.refrigerator.member.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAuthDTO {
    private String code;
    private String email;
}

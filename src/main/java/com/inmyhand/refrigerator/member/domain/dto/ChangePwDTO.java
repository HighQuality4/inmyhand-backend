package com.inmyhand.refrigerator.member.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwDTO {
    private String password1;
    private String password2;
}

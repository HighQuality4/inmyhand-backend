package com.inmyhand.refrigerator.member.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePwDTO {
    private String password1;
    private String password2;
}

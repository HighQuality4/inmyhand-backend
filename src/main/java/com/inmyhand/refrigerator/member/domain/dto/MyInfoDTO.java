package com.inmyhand.refrigerator.member.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoDTO {

    private String email;
    private String nickname;
    private String phoneNum;
}

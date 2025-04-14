package com.inmyhand.refrigerator.member.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {
    private String email;       // 아이디 대체 가능
    private String password;    // 평문 전달 (암호화는 내부 처리)
}


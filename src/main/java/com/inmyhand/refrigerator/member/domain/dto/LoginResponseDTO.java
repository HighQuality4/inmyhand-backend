package com.inmyhand.refrigerator.member.domain.dto;

import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String memberName;
    private String email;
    private String nickname;
    private Date regdate;
    private String providerId;
    private MemberStatus memberStatus;
    private String accessToken;
    private String refreshToken;
}

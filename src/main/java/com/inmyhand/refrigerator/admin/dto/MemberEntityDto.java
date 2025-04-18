package com.inmyhand.refrigerator.admin.dto;

import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;


/**
 * DTO for {@link com.inmyhand.refrigerator.member.domain.entity.MemberEntity}
 */
@Data
@AllArgsConstructor
public class MemberEntityDto implements Serializable {

    @NotBlank
    Long id;

    @NotBlank
    String memberName;

    @NotBlank
    String email;

    @NotBlank
    String nickname;

    @NotBlank
    Date regdate;

    @NotBlank
    String providerId;

    @NotBlank
    MemberStatus status;

    @NotBlank
    String phoneNum;
}
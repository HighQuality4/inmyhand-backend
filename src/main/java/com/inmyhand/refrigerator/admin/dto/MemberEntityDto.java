package com.inmyhand.refrigerator.admin.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for {@link com.inmyhand.refrigerator.member.domain.entity.MemberEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntityDto implements Serializable {

    Long id;

    @NotBlank
    String memberName;

    @NotBlank
    String email;

    @NotBlank
    String nickname;
    
    @JsonFormat(shape = Shape.STRING, pattern =  "yyyy-MM-dd")
    Date regdate;

    @NotBlank
    String providerId;
    
    MemberStatus status;

    @NotBlank
    String phoneNum;
}
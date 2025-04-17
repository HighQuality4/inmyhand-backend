package com.inmyhand.refrigerator.admin.dto;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.inmyhand.refrigerator.member.domain.entity.MemberEntity}
 */
@Value
@AllArgsConstructor
@Data
public class MemberEntityDto implements Serializable {
    Long id;
    String memberName;
    String email;
    String nickname;
    Date regdate;
    String providerId;
    MemberStatus status;
    String phoneNum;
}
package com.inmyhand.refrigerator.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDetailDTO {
    private Long subId;              // 구독 ID
    private String memberName;       // 회원 이름
    private String status;           // 구독 상태
    private Timestamp startDate;     // 시작일
    private Timestamp nextPayDate;   // 다음 결제일
    private Long subplanId;          // 구독 플랜 ID
    private String planName;         // 플랜 이름
    private Integer price;           // 가격
    private Integer paymentInterval; // 결제 간격
    private String intervalUnit;     // 간격 단위
    private Date createdAt;          // 생성일
}
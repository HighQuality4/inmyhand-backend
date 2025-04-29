package com.inmyhand.refrigerator.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    private String subId;            // 구독 ID
    private String memberName;       // 구독자 이름
    private String status;           // 구독 상태
    private Date startDate;     // 구독 시작일
    private Date nextPayDate;   // 다음 결제일
    private String subplanId;        // 구독 플랜 ID
    private String planName;         // 구독 플랜 이름
    private double price;            // 가격
    private int paymentInterval;     // 결제 주기
    private String intervalUnit;     // 결제 주기 단위 (day, month, year 등)
    private Date createdAt; // 생성일시
}
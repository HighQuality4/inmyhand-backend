package com.inmyhand.refrigerator.payment.domain.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentDTO {
    private Long id;
    private String orderId;
    private String paymentKey;
    private String orderName;
    private Integer amount;
    private String method;
    private String status;
    private Timestamp requestAt;
    private Timestamp approvedAt;
    private Timestamp canceledAt;
    private String failReason;
    private Date regdate;
    private Date updatedAt;
    private Long fkUserId;
    private Long fkSubId;
}


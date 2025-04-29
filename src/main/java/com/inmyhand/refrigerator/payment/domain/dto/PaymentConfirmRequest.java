package com.inmyhand.refrigerator.payment.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private String amount;
}

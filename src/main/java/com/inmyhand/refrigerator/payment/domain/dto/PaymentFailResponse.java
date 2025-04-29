package com.inmyhand.refrigerator.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentFailResponse {
    private String code;
    private String message;
}

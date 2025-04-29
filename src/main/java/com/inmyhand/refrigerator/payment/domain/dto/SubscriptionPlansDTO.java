package com.inmyhand.refrigerator.payment.domain.dto;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlansDTO {

    private Long id;
    private String planName;
    private Long price;
    private Integer paymentInterval;
    private String intervalUnit;
    private Date regdate;
}


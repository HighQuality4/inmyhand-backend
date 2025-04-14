package com.inmyhand.refrigerator.subscription.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SubscriptionInfoDTO {

    public SubscriptionInfoDTO(
            Long subId,
            String memberName,
            String status,
            LocalDate startDate,
            LocalDate nextPayDate,
            Long subPlanId,
            String planName,
            Integer price,
            Integer paymentInterval,
            String intervalUnit,
            LocalDateTime regdate
    ) {
        this.subId = subId;
        this.memberName = memberName;
        this.status = status;
        this.startDate = startDate;
        this.nextPayDate = nextPayDate;
        this.subPlanId = subPlanId;
        this.planName = planName;
        this.price = price;
        this.paymentInterval = paymentInterval;
        this.intervalUnit = intervalUnit;
        this.regdate = regdate;
    }

    private Long subId;
    private String memberName;
    private String status;
    private LocalDate startDate;
    private LocalDate nextPayDate;
    private Long subPlanId;
    private String planName;
    private Integer price;
    private Integer paymentInterval;
    private String intervalUnit;
    private LocalDateTime regdate;
}




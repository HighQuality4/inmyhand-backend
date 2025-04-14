package com.inmyhand.refrigerator.subscription.domain.dto;

import java.sql.Timestamp;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {
    private Long id;
    private String status;
    private Timestamp startDate;
    private Timestamp nextPayDate;
    private Timestamp canceledAt;
    private Date regdate;
    private Long fkSubplanId;
    private Long userId;
}


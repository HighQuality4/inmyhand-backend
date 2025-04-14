package com.inmyhand.refrigerator.fridge.domain.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeFoodDTO {
    private Long id;
    private String foodName;
    private Long foodAmount;
    private Date endDate;
    private Date chargeDate;
    private Date saveDate;
    private Long foodCategoryId;
    private Long fridgeId;
}

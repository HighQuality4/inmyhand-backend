package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

import java.util.Date;
import java.util.List;

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
    private List<FridgeDTO> fridgeList;
}

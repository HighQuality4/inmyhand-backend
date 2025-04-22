package com.inmyhand.refrigerator.fridge.domain.dto.food;

import lombok.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

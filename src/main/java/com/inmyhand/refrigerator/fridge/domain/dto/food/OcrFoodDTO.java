package com.inmyhand.refrigerator.fridge.domain.dto.food;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrFoodDTO {

    private String foodName;
    private Long foodAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date chargeDate;
    private String categoryName;
    
    
}

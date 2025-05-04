package com.inmyhand.refrigerator.fridge.domain.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeFridgeFoodDTO {
    private Long id;
    private String foodName;
    private Long foodAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date chargeDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date saveDate;
    private String categoryName;
    private Long fridgeId;

    private String roles;

    
}

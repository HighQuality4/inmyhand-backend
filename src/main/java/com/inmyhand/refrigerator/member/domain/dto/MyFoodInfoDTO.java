package com.inmyhand.refrigerator.member.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFoodInfoDTO {
    private String foodName;
    private String expdate;
}

package com.inmyhand.refrigerator.healthinfo.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthInfoDTO {
    private Long id;
    private String allergy;
    private String hateFood;
    private String interestInfo;
    private Long usersId;
}
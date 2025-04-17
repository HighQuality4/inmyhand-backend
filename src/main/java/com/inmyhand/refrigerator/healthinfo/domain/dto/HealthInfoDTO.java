package com.inmyhand.refrigerator.healthinfo.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthInfoDTO {
    private Long id;
    private List<String> allergy;
    private List<String> hateFood;
    private List<String> interestInfo;
}
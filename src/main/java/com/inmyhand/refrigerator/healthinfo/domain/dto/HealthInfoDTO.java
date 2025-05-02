package com.inmyhand.refrigerator.healthinfo.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonIgnore
    public List<String> getInterestTags(String temp) {
        return Arrays.stream(temp.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
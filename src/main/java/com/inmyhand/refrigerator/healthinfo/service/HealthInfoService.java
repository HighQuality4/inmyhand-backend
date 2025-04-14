package com.inmyhand.refrigerator.healthinfo.service;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HealthInfoService {

    public List<HealthInfoDTO> getAllergyInfo(@Param("memberId") Long memberId);

    public List<HealthInfoDTO> getHateFoodInfo(@Param("memberId") Long memberId);

    public List<HealthInfoDTO> getHealthInterest(@Param("memberId") Long memberId);
}

package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;

import java.util.List;

public interface HealthInfoRepository {

    List<HealthInfoDTO> findAllergyByMemberId(Long memberId);

    List<HealthInfoDTO> findHateFoodByMemberId(Long memberId);

    List<HealthInfoDTO> findInterestInfoByMemberId(Long memberId);
}

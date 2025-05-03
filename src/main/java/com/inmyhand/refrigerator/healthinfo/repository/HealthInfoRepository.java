package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;

import java.util.List;

public interface HealthInfoRepository {

    List<String> findAllInterestInfoCategory();

    List<String> findAllFoodInfoCategory();
}

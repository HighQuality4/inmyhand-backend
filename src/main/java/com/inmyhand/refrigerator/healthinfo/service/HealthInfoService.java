package com.inmyhand.refrigerator.healthinfo.service;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HealthInfoService {

    public List<String> getAllergyInfo(Long memberId);

    public List<String> getHateFoodInfo(Long memberId);

    public List<String> getHealthInterest(Long memberId);

    public List<String> getAllInterestInfoCategory();

    public List<String> getAllRecipeIngredientCategory();
}

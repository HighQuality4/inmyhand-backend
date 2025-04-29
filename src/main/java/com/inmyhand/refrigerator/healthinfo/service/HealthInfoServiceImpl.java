package com.inmyhand.refrigerator.healthinfo.service;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import com.inmyhand.refrigerator.healthinfo.repository.HealthInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthInfoServiceImpl implements HealthInfoService {

    private final HealthInfoRepository healthInfoRepository;

    public List<HealthInfoDTO> getAllergyInfo(@Param("memberId") Long memberId) {
        return healthInfoRepository.findAllergyByMemberId(memberId);
    }

    public List<HealthInfoDTO> getHateFoodInfo(@Param("memberId") Long memberId) {
        return healthInfoRepository.findHateFoodByMemberId(memberId);
    }

    public List<HealthInfoDTO> getHealthInterest(@Param("memberId") Long memberId) {
        return healthInfoRepository.findInterestInfoByMemberId(memberId);
    }

    public List<String> getAllInterestInfoCategory() {
        return healthInfoRepository.findAllInterestInfoCategory();
    }

    public List<String> getAllRecipeIngredientCategory() {
        return healthInfoRepository.findAllFoodInfoCategory();
    }

}

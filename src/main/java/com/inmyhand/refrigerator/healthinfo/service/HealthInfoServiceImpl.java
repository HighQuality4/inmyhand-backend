package com.inmyhand.refrigerator.healthinfo.service;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HateFoodEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.HealthInterestEntity;
import com.inmyhand.refrigerator.healthinfo.domain.entity.MemberAllergyEntity;
import com.inmyhand.refrigerator.healthinfo.repository.AllergyRepository;
import com.inmyhand.refrigerator.healthinfo.repository.HateFoodRepository;
import com.inmyhand.refrigerator.healthinfo.repository.HealthInfoRepository;

import com.inmyhand.refrigerator.healthinfo.repository.HealthInterestRepository;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthInfoServiceImpl implements HealthInfoService {

    private final HealthInfoRepository healthInfoRepository;
    private final AllergyRepository allergyRepository;
    private final HealthInterestRepository healthInterestRepository;
    private final HateFoodRepository hateFoodRepository;
    private final MemberRepository memberRepository;


    public List<String> getAllergyInfo(Long memberId) {
        return allergyRepository.findAllergyByMemberId(memberId);
    }

    public List<String> getHateFoodInfo(@Param("memberId") Long memberId) {
        return hateFoodRepository.findHateFoodByMemberId(memberId);
    }

    public List<String> getHealthInterest(@Param("memberId") Long memberId) {
        return healthInterestRepository.findHealthInterestByMemberId(memberId);
    }

    @Cacheable("healthCategories")
    public List<String> getAllInterestInfoCategory() {
        return healthInfoRepository.findAllInterestInfoCategory();
    }

    @Cacheable("ingredientCategories")
    public List<String> getAllRecipeIngredientCategory() {
        return healthInfoRepository.findAllFoodInfoCategory();
    }

    @Transactional
    public void saveHealthInfo(Long userId, HealthInfoDTO healthInfoDTO) {

        allergyRepository.deleteByMemberId(userId);
        hateFoodRepository.deleteByMemberId(userId);
        healthInterestRepository.deleteByMemberId(userId);

        List<String> interest = healthInfoDTO.getInterestTags(healthInfoDTO.getInterestInfo());
        List<String> hateFood = healthInfoDTO.getInterestTags(healthInfoDTO.getHateFood());
        List<String> allergy = healthInfoDTO.getInterestTags(healthInfoDTO.getAllergy());

        MemberEntity member = memberRepository.findById(userId).orElse(null);

        for (String interestInfo : interest) {
            HealthInterestEntity healthInterestEntity = new HealthInterestEntity();
            healthInterestEntity.setInterestInfo(interestInfo);
            healthInterestEntity.setMemberEntity(member);

            healthInterestRepository.save(healthInterestEntity);
        }

        for (String hateFoodInfo : hateFood) {
            HateFoodEntity hateFoodEntity = new HateFoodEntity();
            hateFoodEntity.setHateFood(hateFoodInfo);
            hateFoodEntity.setMemberEntity(member);

            hateFoodRepository.save(hateFoodEntity);
        }

        for (String allergyInfo : allergy) {
            MemberAllergyEntity memberAllergyEntity = new MemberAllergyEntity();
            memberAllergyEntity.setMemberEntity(member);
            memberAllergyEntity.setAllergy(allergyInfo);

            allergyRepository.save(memberAllergyEntity);
        }
    }



}

package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HealthInfoQueryRepositoryImpl implements HealthInfoRepository {

    private final EntityManager em;

    @Override
    public List<HealthInfoDTO> findAllergyByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, a.allergy, null, null) " +
                                "FROM MemberEntity u JOIN MemberAllergyEntity a ON u.id = a.memberEntity.id " +
                                "WHERE u.id = :memberId", HealthInfoDTO.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<HealthInfoDTO> findHateFoodByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, null, h.hateFood, null) " +
                                "FROM MemberEntity u JOIN HateFoodEntity h ON u.id = h.memberEntity.id " +
                                "WHERE u.id = :memberId", HealthInfoDTO.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<HealthInfoDTO> findInterestInfoByMemberId(Long memberId) {
        return em.createQuery(
                        "SELECT new com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO(u.nickname, null, null, hi.interestInfo) " +
                                "FROM MemberEntity u JOIN HealthInterestEntity hi ON u.id = hi.memberEntity.id " +
                                "WHERE u.id = :memberId", HealthInfoDTO.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}


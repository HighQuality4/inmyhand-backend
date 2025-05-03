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
    public List<String> findAllInterestInfoCategory() {
        return em.createQuery("SELECT h.healthInterestCategoryName FROM HealthInterestCategoryEntity h", String.class).getResultList();
    }

    @Override
    public List<String> findAllFoodInfoCategory() {
        return em.createQuery("SELECT r.ingredientName FROM RecipeIngredientEntity r", String.class).getResultList();
    }

}
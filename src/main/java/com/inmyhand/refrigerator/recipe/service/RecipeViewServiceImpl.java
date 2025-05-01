package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeViewsEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeViewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RecipeViewServiceImpl implements RecipeViewService {

    private final RecipeViewsRepository recipeViewsRepository;
    private final MemberRepository memberRepository;
    private final RecipeInfoRepository recipeInfoRepository;

    /**
     * 레시피 조회 기록 추가
     */
    @Transactional
    public void addRecipeView(Long memberId, Long recipeId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + memberId));

        RecipeInfoEntity recipe = recipeInfoRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다. ID: " + recipeId));

        RecipeViewsEntity recipeView = new RecipeViewsEntity();
        recipeView.setMemberEntity(member);
        recipeView.setRecipeInfoEntity(recipe);
        recipeView.setViewedAt(new Date());

        recipeViewsRepository.save(recipeView);

        // 양방향 관계 설정
        member.addRecipeView(recipeView);
    }

}

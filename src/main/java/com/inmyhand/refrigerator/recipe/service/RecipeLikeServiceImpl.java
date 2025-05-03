package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeLikesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RecipeLikeServiceImpl implements RecipeLikeService {

    private final MemberRepository memberRepository;
    private final RecipeInfoRepository recipeInfoRepository;
    private final RecipeLikesRepository recipeLikesRepository;

    /**
     * 레시피 좋아요 토글 (추가/삭제)
     * @param memberId 멤버 ID
     * @param recipeId 레시피 ID
     * @return 좋아요 상태 (true: 좋아요 추가됨, false: 좋아요 취소됨)
     */
    @Transactional
    public boolean toggleRecipeLike(Long memberId, Long recipeId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + memberId));

        RecipeInfoEntity recipe = recipeInfoRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다. ID: " + recipeId));

        // 이미 좋아요를 눌렀는지 확인
        Optional<RecipeLikesEntity> existingLike = recipeLikesRepository.findByMemberEntityAndRecipeInfoEntity(member, recipe);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태면 제거
            RecipeLikesEntity like = existingLike.get();
            member.removeRecipeLike(like);
            recipeLikesRepository.delete(like);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요를 누르지 않은 상태면 추가
            RecipeLikesEntity recipeLike = new RecipeLikesEntity();
            recipeLike.setMemberEntity(member);
            recipeLike.setRecipeInfoEntity(recipe);
            recipeLike.setLikedAt(new Date());

            // 먼저 저장 후 양방향 관계 설정
            recipeLikesRepository.save(recipeLike);
            member.addRecipeLike(recipeLike);

            return true; // 좋아요 추가됨
        }
    }

    // 좋아요 조회
    public boolean isRecipeLikedByMember(Long memberId, Long recipeId) {
        return recipeLikesRepository.existsByMemberEntityIdAndRecipeInfoEntityId(memberId, recipeId);
    }
}

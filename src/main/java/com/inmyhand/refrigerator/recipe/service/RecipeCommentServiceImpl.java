package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeCommentRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeCommentServiceImpl implements RecipeCommentService {

    private final RecipeCommentRepository recipeCommentRepository;
    private final MemberRepository memberRepository;
    private final RecipeInfoRepository recipeInfoRepository;

    /**
     * 댓글 추가 메서드
     * @param commentContents 댓글 내용
     * @param memberId 회원 ID
     * @param recipeId 레시피 ID
     */
    @Override
    @Transactional
    public void addComment(String commentContents, Long memberId, Long recipeId) {
        // 회원과 레시피 조회
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다. ID: " + memberId));

        RecipeInfoEntity recipe = recipeInfoRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다. ID: " + recipeId));

        // 댓글 엔티티 생성 및 설정
        RecipeCommentEntity comment = new RecipeCommentEntity();
        comment.setCommentContents(commentContents);

        // 양방향 관계 설정 (이미 구현된 setMemberEntity 메서드 활용)
        comment.setMemberEntity(member);
        comment.setRecipeInfoEntity(recipe);

        // 저장
        recipeCommentRepository.save(comment);
    }

    /**
     * 댓글 삭제 메서드
     * @param commentId 댓글 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        RecipeCommentEntity comment = recipeCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId));

        // 연관관계 제거 (양방향 관계 정리)
        MemberEntity member = comment.getMemberEntity();
        if (member != null) {
            member.getRecipeCommentList().remove(comment);
        }

        RecipeInfoEntity recipe = comment.getRecipeInfoEntity();
        if (recipe != null) {
            recipe.getRecipeCommentList().remove(comment);
        }

        // 댓글 삭제
        recipeCommentRepository.delete(comment);
    }
}

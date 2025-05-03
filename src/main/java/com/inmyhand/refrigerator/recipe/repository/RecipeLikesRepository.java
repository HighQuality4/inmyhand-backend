package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeLikesRepository extends JpaRepository<RecipeLikesEntity, Long> {
    // 좋아요 체크한 레시피 조회
    List<RecipeLikesEntity> findByMemberEntity_Id(Long id);


    Optional<RecipeLikesEntity> findByMemberEntityAndRecipeInfoEntity(MemberEntity memberEntity, RecipeInfoEntity recipeInfoEntity);
    Boolean existsByMemberEntityIdAndRecipeInfoEntityId(Long memberEntity_id, Long recipeInfoEntity_id);

    @Query("""
        SELECT new com.inmyhand.refrigerator.member.domain.dto.MyLikedRecipeDTO(
            rl.recipeInfoEntity.id,
            rl.memberEntity.nickname,
            rl.recipeInfoEntity.recipeName,
            rl.recipeInfoEntity.createdAt
        )
        FROM RecipeLikesEntity rl
        WHERE rl.memberEntity.id = :memberId
    """)
    Page<RecipeLikesEntity> findByMemberEntity_Id(Long memberId, Pageable pageable);


// 좋아요 생성

// 좋아요 삭제
}

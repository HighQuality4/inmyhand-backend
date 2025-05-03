package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.member.domain.dto.MyLikedRecipeDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeLikesRepository extends JpaRepository<RecipeLikesEntity, Long> {
    // 좋아요 체크한 레시피 조회
    List<RecipeLikesEntity> findByMemberEntity_Id(Long id);


    Optional<RecipeLikesEntity> findByMemberEntityAndRecipeInfoEntity(MemberEntity memberEntity, RecipeInfoEntity recipeInfoEntity);
    Boolean existsByMemberEntityIdAndRecipeInfoEntityId(Long memberEntity_id, Long recipeInfoEntity_id);

    @Query("""
        SELECT new com.inmyhand.refrigerator.member.domain.dto.MyLikedRecipeDTO(
            ri.id,
            m.nickname,
            ri.recipeName,
            rl.likedAt
        )
        FROM RecipeLikesEntity rl
        JOIN rl.memberEntity m
        JOIN rl.recipeInfoEntity ri
        WHERE m.id = :memberId
    """)
    Page<MyLikedRecipeDTO> findByMemberEntity_Id(@Param("memberId") Long memberId, Pageable pageable);


// 좋아요 생성

// 좋아요 삭제
}

package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto;
import com.inmyhand.refrigerator.member.domain.dto.MyRecipeDTO;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface RecipeInfoRepository extends JpaRepository<RecipeInfoEntity, Long>, CustomQueryRecipeInfoRepository {
    // 모든 레시피 목록 조회 - 기본으로 최신 레시피가 제일 첫번째에 오도록 함
    Page<RecipeInfoEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 인기 레시피 - 제일 좋아요가 많은 레시피 N개 추출
    @Query("SELECT r FROM RecipeInfoEntity r LEFT JOIN r.recipeLikesList l GROUP BY r.id ORDER BY COUNT(l) DESC")
    List<RecipeInfoEntity> findTop5ByOrderByLikesCountDesc(Pageable pageable);

    // 내가 등록한 레시피 조회
    List<RecipeInfoEntity> findByMemberEntityId(Long memberEntityId);

    // 레시피 검색 조회 - 페이징
    Page<RecipeInfoEntity> findByRecipeNameContaining(String keyword, Pageable pageable);

    // 레시피 이름 관련 모두 찾기
    List<RecipeInfoEntity> findByRecipeNameContaining(String keyword);

    //레시피 이름으로 찾기
    Optional<RecipeInfoEntity> findByRecipeName(String recipeName);

    @Query("SELECT new com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto(" +
            "r.id, r.recipeName, r.createdAt, " +
            "SIZE(r.recipeLikesList), SIZE(r.recipeViewsList)) " +
            "FROM RecipeInfoEntity r " +
            "WHERE r.memberEntity.id = :id " +
            "ORDER BY r.createdAt ASC")
    Page<AdminRecipeInfoDto> findAdminRecipeInfoUser(@Param("id") Long id, Pageable pageable);

    Optional<RecipeInfoEntity> findFirstByRecipeName(String keyword);


    @Query("SELECT r FROM RecipeInfoEntity r JOIN FETCH r.filesEntities")
    List<RecipeInfoEntity> similarFindAll();

    // findById 대체 메서드
    @Query("SELECT r FROM RecipeInfoEntity r JOIN FETCH r.filesEntities WHERE r.id = :id")
    Optional<RecipeInfoEntity> similarFindById(Long id);

    @Query("""
        SELECT new com.inmyhand.refrigerator.member.domain.dto.MyRecipeDTO(
            m.nickname,
            r.recipeName,
            r.createdAt
        )
        FROM RecipeInfoEntity r
        JOIN r.memberEntity m
        WHERE m.id = :memberId
    """)
    Page<MyRecipeDTO> findByMemberEntity_Id(@Param("memberId") Long memberId, Pageable pageable);
}


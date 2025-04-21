package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeLikesRepository extends JpaRepository<RecipeLikesEntity, Long> {
    // 좋아요 체크한 레시피 조회
    List<RecipeLikesEntity> findByMemberEntity_Id(Long id);

    // 좋아요 생성

    // 좋아요 삭제
}

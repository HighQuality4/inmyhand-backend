package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.mapper.RecipeDetailMapper;
import com.inmyhand.refrigerator.recipe.mapper.RecipeSummaryMapper;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeLikesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 조인 기반 상세 조회, 리스트 조회 전용
@Service
@RequiredArgsConstructor
public class RecipeQueryServiceImpl implements RecipeQueryService {
    @Autowired
    private RecipeInfoRepository infoRepository;
    @Autowired
    private RecipeLikesRepository likesRepository;

    private final RecipeSummaryMapper summaryMapper;
    private final RecipeDetailMapper detailMapper;

    // 모든 레시피 목록 조회
    public Page<RecipeSummaryDTO> getAllRecipeList(Pageable pageable) {
        Page<RecipeInfoEntity> recipePage = infoRepository.findAllByOrderByCreatedAtDesc(pageable);
        return recipePage.map(summaryMapper::toDto);
    }

    // 인기 레시피 목록 조회
    public List<RecipeSummaryDTO> getPopularRecipeList() {
        List<RecipeInfoEntity> recipesList = infoRepository.findTop5ByOrderByLikesCountDesc(PageRequest.of(0, 5));

        List<RecipeInfoEntity> recipes = recipesList.stream()
                .filter(r -> r.getRecipeLikesList() != null && !r.getRecipeLikesList().isEmpty())
                .limit(5)
                .toList();

        return summaryMapper.toDtoList(recipes);
    }

    // 레시피 정렬 (난이도, 칼로리 오름차순/내림차순)
    public Page<RecipeSummaryDTO> getArrayRecipeList(String orderBy, String sortType, int page, int size) {
        Sort.Direction direction = Sort.Direction.fromString(sortType.toUpperCase());
        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RecipeInfoEntity> recipePage = infoRepository.findAll(pageable);
        return recipePage.map(summaryMapper::toDto);
    }

    // 레시피 상세
    public RecipeDetailDTO getRecipeDetail(Long recipeId) {
        RecipeInfoEntity recipe = infoRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다."));

        return detailMapper.toDto(recipe);
    }

    // 레시피 검색
    public Page<RecipeSummaryDTO> getSearchRecipeList(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeInfoEntity> recipes = infoRepository.findByRecipeNameContaining(keyword, pageable);
        return recipes.map(summaryMapper::toDto);
    }

    // TODO : 내가 등록한 레시피 조회
    public List<RecipeSummaryDTO> getMyRecipeList(Long userId) {
        List<RecipeInfoEntity> recipes = infoRepository.findByMemberEntityId(userId);

        return summaryMapper.toDtoList(recipes);
    }

    // TODO : 좋아요 체크한 레시피 조회
    public List<RecipeSummaryDTO> getMyLikeRecipeList(Long userId) {
        List<RecipeLikesEntity> likesEntities = likesRepository.findByMemberEntity_Id(userId);

        List<RecipeInfoEntity> likedRecipes = likesEntities.stream()
                .map(RecipeLikesEntity::getRecipeInfoEntity)
                .collect(Collectors.toList());

        return summaryMapper.toDtoList(likedRecipes);
    }

}

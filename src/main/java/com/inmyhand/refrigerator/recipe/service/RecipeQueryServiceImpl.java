package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.recipe.domain.dto.*;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity;
import com.inmyhand.refrigerator.recipe.mapper.RecipeSummaryMapper;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeLikesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // 모든 레시피 목록 조회
    public List<RecipeSummaryDTO> getAllRecipeList() {
        List<RecipeInfoEntity> recipes = infoRepository.findAllByOrderByCreatedAtDesc();

        return summaryMapper.toDtoList(recipes);
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

    // 레시피 정렬 (난이도, 소요시간, 칼로리 오름차순/내림차순)
    public List<RecipeSummaryDTO> getArrayRecipeList(String orderBy, String sortType) {
        Sort.Direction direction = Sort.Direction.fromString(sortType.toUpperCase());

        Sort sort = Sort.by(direction, orderBy);
        List<RecipeInfoEntity> recipes = infoRepository.findAll(sort);

        return summaryMapper.toDtoList(recipes);
    }

    // 레시피 상세
    public RecipeDetailDTO getRecipeDetail(Long recipeId) {
        RecipeInfoEntity recipe = infoRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다."));

        List<RecipeCategoryEntityDto> categories = recipe.getRecipeCategoryList().stream()
                .map(c -> new RecipeCategoryEntityDto(
                        c.getId(),
                        c.getRecipeCategoryName(),
                        c.getRecipeCategoryType()))
                .collect(Collectors.toList());

        List<String> fileUrls = recipe.getFilesEntities().stream()
                .map(file -> file.getFileUrl())
                .collect(Collectors.toList());

        List<RecipeIngredientEntityDto> ingredients = recipe.getRecipeIngredientList().stream()
                .map(i -> new RecipeIngredientEntityDto(
                        i.getId(),
                        i.getIngredientName(),
                        i.getIngredientGroup(),
                        i.getIngredientQuantity(),
                        i.getIngredientUnit())
                )
                .collect(Collectors.toList());

        List<RecipeStepsEntityDto> steps = recipe.getRecipeStepsList().stream()
                .map(s -> new RecipeStepsEntityDto(
                        s.getId(),
                        s.getStepNumber(),
                        s.getStepDescription(),
                        s.getFilesEntity().getFileUrl()
                        )
                )
                .collect(Collectors.toList());

        List<RecipeCommentEntityDto> comments = recipe.getRecipeCommentList().stream()
                .map(c -> new RecipeCommentEntityDto(
                                c.getId(),
                                c.getMemberEntity().getNickname(),
                                c.getCommentContents(),
                                c.getCreatedAt()
                        )
                )
                .collect(Collectors.toList());

        RecipeNutrientAnalysisEntity analysisEntity = recipe.getRecipeNutrientAnalysisList()
                .stream()
                .findFirst()
                .orElse(null);

        RecipeNutrientAnalysisEntityDto analysis = null;
        if (analysisEntity != null) {
            analysis = new RecipeNutrientAnalysisEntityDto(
                    analysisEntity.getId(),
                    analysisEntity.getAnalysisResult(),
                    analysisEntity.getScore(),
                    analysisEntity.getCarbs(),
                    analysisEntity.getProtein(),
                    analysisEntity.getFat(),
                    analysisEntity.getMineral(),
                    analysisEntity.getVitamin()
            );
        }

        Long parentRecipeId = recipe.getParentRecipe() != null
                ? recipe.getParentRecipe().getId()
                : null;

        return new RecipeDetailDTO(
                recipe.getId(),
                parentRecipeId,
                recipe.getRecipeName(),
                recipe.getDifficulty(),
                recipe.getCookingTime().getLabel(),
                recipe.getCalories(),
                recipe.getSummary(),
                recipe.getServings(),
                recipe.getRecipeDepth(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt(),
                recipe.getMemberEntity().getNickname(),
                (long) recipe.getRecipeLikesList().size(),
                (long) recipe.getRecipeViewsList().size(),
                categories,
                fileUrls,
                ingredients,
                steps,
                comments,
                analysis
        );
    }

    // 레시피 검색
    public List<RecipeSummaryDTO> getSearchRecipeList(String keyword) {
        List<RecipeInfoEntity> recipes = infoRepository.findByRecipeNameContaining(keyword).orElse(new ArrayList<>());
        return summaryMapper.toDtoList(recipes);
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

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.*;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeDetailMapper {

    private final RecipeCategoryMapper categoryMapper;
    private final RecipeIngredientMapper ingredientMapper;
    private final RecipeStepsMapper stepsMapper;
    private final RecipeCommentMapper commentMapper;
    private final RecipeNutrientAnalysisMapper nutrientAnalysisMapper;


    /**
     * RecipeInfoEntity -> RecipeDetailDTO
     * 레시피 상세 정보 DTO 변환 (모든 연관 정보 포함)
     *
     * @param entity 레시피 정보 엔티티
     * @return RecipeDetailDTO
     */
    public RecipeDetailDTO toDto(RecipeInfoEntity entity) {
        if (entity == null) {
            return null;
        }

        RecipeDetailDTO dto = new RecipeDetailDTO();
        
        // 기본 레시피 정보 설정
        dto.setId(entity.getId());
        dto.setRecipeName(entity.getRecipeName());
        dto.setDifficulty(entity.getDifficulty());
        dto.setCookingTime(entity.getCookingTime().getLabel());
        dto.setCalories(entity.getCalories());
        dto.setSummary(entity.getSummary());
        dto.setServings(entity.getServings());
        dto.setRecipeDepth(entity.getRecipeDepth());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // 부모 레시피
        Long parentRecipeId = entity.getParentRecipe() != null
                ? entity.getParentRecipe().getId()
                : null;

        dto.setParentRecipeId(parentRecipeId);

        // 사용자 닉네임
        if (entity.getMemberEntity() != null) {
            dto.setUserNickname(entity.getMemberEntity().getNickname());
        }

        // 사용자 사진
        String profileUrl = null;
        FilesEntity profileFile = entity.getMemberEntity().getFilesEntity();
        if (profileFile != null) {
            profileUrl = profileFile.getFileUrl();
            dto.setUserProfileImageUrl(profileUrl);
        }
        
        // 좋아요 개수
        dto.setLikeCount(entity.getRecipeLikesList() != null ? 
                (long) entity.getRecipeLikesList().size() : 0L);
        
        // 조회수
        dto.setViewCount(entity.getRecipeViewsList() != null ? 
                (long) entity.getRecipeViewsList().size() : 0L);
        
        // 카테고리 매핑
        if (entity.getRecipeCategoryList() != null) {
            dto.setCategories(categoryMapper.toDtoList(entity.getRecipeCategoryList()));
        } else {
            dto.setCategories(Collections.emptyList());
        }
        
        // 이미지 URL 매핑
        if (entity.getFilesEntities() != null && !entity.getFilesEntities().isEmpty()) {
            dto.setFileUrl(entity.getFilesEntities().get(0).getFileUrl());
        } else {
            dto.setFileUrl(null);
        }
        
        // 재료 매핑
        if (entity.getRecipeIngredientList() != null) {
            dto.setIngredients(ingredientMapper.toDtoList(entity.getRecipeIngredientList()));
        } else {
            dto.setIngredients(Collections.emptyList());
        }
        
        // 요리 단계 매핑
        if (entity.getRecipeStepsList() != null) {
            dto.setSteps(stepsMapper.toDtoList(entity.getRecipeStepsList()));
        } else {
            dto.setSteps(Collections.emptyList());
        }
        
        // 댓글 매핑
        if (entity.getRecipeCommentList() != null) {
            dto.setComments(commentMapper.toDtoList(entity.getRecipeCommentList()));
        } else {
            dto.setComments(Collections.emptyList());
        }
        
        // 영양 분석 매핑 (가장 최근 분석 결과 사용)
        if (entity.getRecipeNutrientAnalysisList() != null && !entity.getRecipeNutrientAnalysisList().isEmpty()) {
            // 영양 분석 엔티티 -> DTO 변환
            List<RecipeNutrientAnalysisEntityDto> analysisDtos =
                    nutrientAnalysisMapper.toDtoList(entity.getRecipeNutrientAnalysisList());
            
            // 가장 최근 영양 분석 결과 선택하거나 처리 로직
            if (!analysisDtos.isEmpty()) {
                RecipeNutrientAnalysisEntityDto analysisEntityDto = analysisDtos.get(0);
                
                // RecipeNutrientAnalysisDTO 생성 및 값 설정
                RecipeNutrientAnalysisEntityDto analysisDto = new RecipeNutrientAnalysisEntityDto();
                analysisDto.setAnalysisResult(analysisEntityDto.getAnalysisResult());
                analysisDto.setFitnessScore(analysisEntityDto.getFitnessScore());
                analysisDto.setCarbohydrate(analysisEntityDto.getCarbohydrate());
                analysisDto.setProtein(analysisEntityDto.getProtein());
                analysisDto.setFat(analysisEntityDto.getFat());
                analysisDto.setMineral(analysisEntityDto.getMineral());
                analysisDto.setVitamin(analysisEntityDto.getVitamin());
                
                dto.setAnalysis(analysisDto);
            }
        }
        
        return dto;
    }
    
    /**
     * List<RecipeInfoEntity> -> List<RecipeDetailDTO>
     *
     * @param entities 레시피 정보 엔티티 목록
     * @return List<RecipeDetailDTO>
     */
    public List<RecipeDetailDTO> toDtoList(List<RecipeInfoEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    // toEntity 메서드는 필요에 따라 구현 
    // (RecipeDetailDTO는 통합 정보를 포함하는 응답용 DTO이므로 
    // Entity로 변환하는 경우는 드물지만, 필요하다면 구현할 수 있음)
}

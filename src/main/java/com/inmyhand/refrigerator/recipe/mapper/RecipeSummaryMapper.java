package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCategoryEntityDto;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeSummaryMapper {

    private final RecipeCategoryMapper categoryMapper;


    /**
     * RecipeInfoEntity -> RecipeSummaryDTO
     * 레시피 엔티티에서 요약 정보 추출
     *
     * @param entity 레시피 정보 엔티티
     * @return RecipeSummaryDTO
     */
    public RecipeSummaryDTO toDto(RecipeInfoEntity entity) {
        if (entity == null) {
            return null;
        }

        RecipeSummaryDTO dto = new RecipeSummaryDTO();
        
        // 기본 레시피 정보 설정
        dto.setId(entity.getId());
        dto.setRecipeName(entity.getRecipeName());
        dto.setDifficulty(entity.getDifficulty());
        dto.setCookingTime(entity.getCookingTime() != null ? entity.getCookingTime().getLabel() : null);
        dto.setCalories(entity.getCalories());
        
        // 닉네임 설정
        if (entity.getMemberEntity() != null) {
            dto.setUserNickname(entity.getMemberEntity().getNickname());
        }
        
        // 좋아요 개수
        dto.setLikeCount(entity.getRecipeLikesList() != null ? 
                (long) entity.getRecipeLikesList().size() : 0L);
        
        // 이미지 URL 매핑 - 대표 이미지만 포함할 수도 있음
        if (entity.getFilesEntities() != null) {
            dto.setFileUrl(entity.getFilesEntities().stream()
                    .map(FilesEntity::getFileUrl)
                    .collect(Collectors.toList()));
        } else {
            dto.setFileUrl(Collections.emptyList());
        }
        
        // 카테고리 매핑
        if (entity.getRecipeCategoryList() != null) {
            // RecipeCategoryEntityDto -> RecipeCategoryDTO 변환이 필요할 수 있음
            // 여기서는 간단하게 매핑하는 방식으로 처리
            List<RecipeCategoryEntityDto> categoryDTOs = entity.getRecipeCategoryList().stream()
                    .map(categoryEntity -> {
                        RecipeCategoryEntityDto categoryDTO = new RecipeCategoryEntityDto(
                                categoryEntity.getId(),
                                categoryEntity.getRecipeCategoryName(),
                                categoryEntity.getRecipeCategoryType()
                        );
                        return categoryDTO;
                    })
                    .collect(Collectors.toList());
            
            dto.setCategories(categoryDTOs);
        } else {
            dto.setCategories(Collections.emptyList());
        }

        // 사용자 프로필 사진 매핑
        String profileUrl = null;
        FilesEntity profileFile = entity.getMemberEntity().getFilesEntity();
        if (profileFile != null) {
            profileUrl = profileFile.getFileUrl();
            dto.setUserProfileImageUrl(profileUrl);
        }
        
        return dto;
    }
    
    /**
     * List<RecipeInfoEntity> -> List<RecipeSummaryDTO>
     *
     * @param entities 레시피 정보 엔티티 목록
     * @return List<RecipeSummaryDTO>
     */
    public List<RecipeSummaryDTO> toDtoList(List<RecipeInfoEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

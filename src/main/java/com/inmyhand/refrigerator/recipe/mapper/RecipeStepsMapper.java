package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto2.RecipeStepsEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeStepsMapper {

    /**
     * DTO -> RecipeStepsEntity
     * (RecipeInfoEntity와 FilesEntity는 별도로 설정해야 함)
     *
     * @param dto 레시피 단계 DTO
     * @return RecipeStepsEntity
     */
    public RecipeStepsEntity toEntity(RecipeStepsEntityDto dto) {
        if (dto == null) {
            return null;
        }
        
        RecipeStepsEntity entity = new RecipeStepsEntity();
        entity.setId(dto.getId());
        entity.setStepNumber(dto.getStepNumber());
        entity.setStepDescription(dto.getStepDescription());
        
        // fileUrl은 FilesEntity에 속하므로 여기서 직접 설정할 수 없음
        // 서비스 레이어에서 별도로 처리해야 함
        
        return entity;
    }


    /**
     * RecipeStepsEntity -> DTO
     * (filesEntity가 있으면 fileUrl 정보 포함)
     *
     * @param entity 레시피 단계 엔티티
     * @return RecipeStepsEntityDto
     */
    public RecipeStepsEntityDto toDto(RecipeStepsEntity entity) {
        if (entity == null) {
            return null;
        }
        
        RecipeStepsEntityDto dto = new RecipeStepsEntityDto();
        dto.setId(entity.getId());
        dto.setStepNumber(entity.getStepNumber());
        dto.setStepDescription(entity.getStepDescription());
        
        // filesEntity가 있으면 fileUrl 설정
        if (entity.getFilesEntity() != null) {
            dto.setFileUrl(entity.getFilesEntity().getFileUrl());
        }
        
        return dto;
    }

    /**
     * List<RecipeStepsEntity> -> List<DTO>
     *
     * @param entities 레시피 단계 엔티티 목록
     * @return List<RecipeStepsEntityDto>
     */
    public List<RecipeStepsEntityDto> toDtoList(List<RecipeStepsEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeStepsEntity>
     * (RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dtos 레시피 단계 DTO 목록
     * @return List<RecipeStepsEntity>
     */
    public List<RecipeStepsEntity> toEntityList(List<RecipeStepsEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeViewsEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeViewsEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeViewsMapper {

    /**
     * DTO -> RecipeViewsEntity
     * (RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dto 조회수 DTO
     * @return RecipeViewsEntity
     */
    public RecipeViewsEntity toEntity(RecipeViewsEntityDto dto) {
        if (dto == null) {
            return null;
        }
        
        RecipeViewsEntity entity = new RecipeViewsEntity();
        entity.setId(dto.getId());
        entity.setViewedAt(dto.getViewedAt());
        
        return entity;
    }
    

    /**
     * RecipeViewsEntity -> DTO
     *
     * @param entity 조회수 엔티티
     * @return RecipeViewsEntityDto
     */
    public RecipeViewsEntityDto toDto(RecipeViewsEntity entity) {
        if (entity == null) {
            return null;
        }
        
        RecipeViewsEntityDto dto = new RecipeViewsEntityDto();
        dto.setId(entity.getId());
        dto.setViewedAt(entity.getViewedAt());
        
        return dto;
    }

    /**
     * List<RecipeViewsEntity> -> List<DTO>
     *
     * @param entities 조회수 엔티티 목록
     * @return List<RecipeViewsEntityDto>
     */
    public List<RecipeViewsEntityDto> toDtoList(List<RecipeViewsEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeViewsEntity>
     * (RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dtos 조회수 DTO 목록
     * @return List<RecipeViewsEntity>
     */
    public List<RecipeViewsEntity> toEntityList(List<RecipeViewsEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCategoryEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeCategoryMapper {

    /**
     * DTO -> RecipeCategoryEntity
     *
     * @param dto
     * @return RecipeCategoryEntity
     */
    public RecipeCategoryEntity toEntity(RecipeCategoryEntityDto dto) {
        RecipeCategoryEntity entity = new RecipeCategoryEntity();
        entity.setId(dto.getId());
        entity.setRecipeCategoryName(dto.getRecipeCategoryName());
        entity.setRecipeCategoryType(dto.getRecipeCategoryType());
        return entity;
    }

    /**
     * RecipeCategoryEntity -> DTO
     *
     * @param entity
     * @return RecipeCategoryEntityDto
     */
    public RecipeCategoryEntityDto toDto(RecipeCategoryEntity entity) {
        return new RecipeCategoryEntityDto(
                entity.getId(),
                entity.getRecipeCategoryName(),
                entity.getRecipeCategoryType()
        );
    }

    /**
     * List<RecipeCategoryEntity> -> List<DTO>
     *
     * @param entities
     * @return List<RecipeCategoryEntityDto>
     */
    public List<RecipeCategoryEntityDto> toDtoList(List<RecipeCategoryEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeCategoryEntity>
     *
     * @param dtos
     * @return List<RecipeCategoryEntity>
     */
    public List<RecipeCategoryEntity> toEntityList(List<RecipeCategoryEntityDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

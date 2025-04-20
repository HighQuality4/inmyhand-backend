package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCommentEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeCommentMapper {


    /**
     * DTO -> RecipeCommentEntity
     * (양방향 관계에서 RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dto
     * @return RecipeCommentEntity
     */
    public RecipeCommentEntity toEntity(RecipeCommentEntityDto dto) {
        if (dto == null) {
            return null;
        }

        RecipeCommentEntity entity = new RecipeCommentEntity();
        entity.setId(dto.getId());
        entity.setCommentContents(dto.getCommentContents());
        entity.setCreatedAt(dto.getCreatedAt());

        return entity;
    }

    /**
     * RecipeCommentEntity -> DTO
     * (RecipeInfoEntity 관계는 순환 참조 방지를 위해 무시)
     *
     * @param entity
     * @return RecipeCommentEntityDto
     */
    public RecipeCommentEntityDto toDto(RecipeCommentEntity entity) {
        if (entity == null) {
            return null;
        }

        RecipeCommentEntityDto dto = new RecipeCommentEntityDto();
        dto.setId(entity.getId());
        dto.setCommentContents(entity.getCommentContents());
        dto.setCreatedAt(entity.getCreatedAt());
        if(entity.getMemberEntity() != null) {
            dto.setNickname(entity.getMemberEntity().getNickname());
        }

        return dto;
    }

    /**
     * List<RecipeCommentEntity> -> List<DTO>
     *
     * @param entities
     * @return List<RecipeCommentEntityDto>
     */
    public List<RecipeCommentEntityDto> toDtoList(List<RecipeCommentEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    /**
     * 기본 toEntityList 메서드 (RecipeInfoEntity 없이 호출 시)
     */
    public List<RecipeCommentEntity> toEntityList(List<RecipeCommentEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto2.RecipeLikesEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeLikesMapper {

    /**
     * DTO -> RecipeLikesEntity (기본)
     * 참고: memberEntity와 recipeInfoEntity는 별도로 설정해야 함
     *
     * @param dto 좋아요 DTO
     * @return RecipeLikesEntity
     */
    public RecipeLikesEntity toEntity(RecipeLikesEntityDto dto) {
        if (dto == null) {
            return null;
        }
        
        RecipeLikesEntity entity = new RecipeLikesEntity();
        entity.setId(dto.getId());
        entity.setLikedAt(dto.getLikedAt());
        // memberEntity와 recipeInfoEntity는 이 메서드에서 설정하지 않음
        
        return entity;
    }
    

    /**
     * RecipeLikesEntity -> DTO
     *
     * @param entity 좋아요 엔티티
     * @return RecipeLikesEntityDto
     */
    public RecipeLikesEntityDto toDto(RecipeLikesEntity entity) {
        if (entity == null) {
            return null;
        }
        
        RecipeLikesEntityDto dto = new RecipeLikesEntityDto();
        dto.setId(entity.getId());
        dto.setLikedAt(entity.getLikedAt());
        
        // memberEntity의 id를 likeMemberId로 설정
        if (entity.getMemberEntity() != null) {
            dto.setLikeMemberId(entity.getMemberEntity().getId());
        }
        
        return dto;
    }

    /**
     * List<RecipeLikesEntity> -> List<DTO>
     *
     * @param entities 좋아요 엔티티 목록
     * @return List<RecipeLikesEntityDto>
     */
    public List<RecipeLikesEntityDto> toDtoList(List<RecipeLikesEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeLikesEntity>
     * 참고: memberEntity와 recipeInfoEntity는 별도로 설정해야 함
     *
     * @param dtos 좋아요 DTO 목록
     * @return List<RecipeLikesEntity>
     */
    public List<RecipeLikesEntity> toEntityList(List<RecipeLikesEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * List<DTO> -> List<RecipeLikesEntity> with RecipeInfoEntity
     * memberEntity는 각 DTO의 likeMemberId로 조회해야 함 (서비스 레이어에서 처리)
     *
     * @param dtos 좋아요 DTO 목록
     * @param recipeInfoEntity 연결할 레시피 정보 엔티티
     * @return List<RecipeLikesEntity>
     */
    public List<RecipeLikesEntity> toEntityList(List<RecipeLikesEntityDto> dtos, RecipeInfoEntity recipeInfoEntity) {
        if (dtos == null) {
            return null;
        }
        
        List<RecipeLikesEntity> entities = toEntityList(dtos);
        
        // 양방향 관계 설정
        if (recipeInfoEntity != null) {
            entities.forEach(entity -> entity.setRecipeInfoEntity(recipeInfoEntity));
        }
        
        return entities;
    }
}

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.recipe.domain.dto2.RecipeIngredientEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeIngredientMapper {

    /**
     * DTO -> RecipeIngredientEntity
     * (양방향 관계의 경우 RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dto 재료 DTO
     * @return RecipeIngredientEntity
     */
    public RecipeIngredientEntity toEntity(RecipeIngredientEntityDto dto) {
        if (dto == null) {
            return null;
        }
        
        RecipeIngredientEntity entity = new RecipeIngredientEntity();
        entity.setId(dto.getId());
        entity.setIngredientName(dto.getIngredientName());
        entity.setIngredientGroup(dto.getIngredientGroup());
        entity.setIngredientQuantity(dto.getIngredientQuantity());
        entity.setIngredientUnit(dto.getIngredientUnit());
        
        return entity;
    }

    /**
     * RecipeIngredientEntity -> DTO
     *
     * @param entity 재료 엔티티
     * @return RecipeIngredientEntityDto
     */
    public RecipeIngredientEntityDto toDto(RecipeIngredientEntity entity) {
        if (entity == null) {
            return null;
        }
        
        RecipeIngredientEntityDto dto = new RecipeIngredientEntityDto();
        dto.setId(entity.getId());
        dto.setIngredientName(entity.getIngredientName());
        dto.setIngredientGroup(entity.getIngredientGroup());
        dto.setIngredientQuantity(entity.getIngredientQuantity());
        dto.setIngredientUnit(entity.getIngredientUnit());
        
        return dto;
    }

    /**
     * List<RecipeIngredientEntity> -> List<DTO>
     *
     * @param entities 재료 엔티티 목록
     * @return List<RecipeIngredientEntityDto>
     */
    public List<RecipeIngredientEntityDto> toDtoList(List<RecipeIngredientEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeIngredientEntity>
     *
     * @param dtos 재료 DTO 목록
     * @return List<RecipeIngredientEntity>
     */
    public List<RecipeIngredientEntity> toEntityList(List<RecipeIngredientEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * List<DTO> -> List<RecipeIngredientEntity> with RecipeInfoEntity
     * (양방향 관계 설정)
     *
     * @param dtos 재료 DTO 목록
     * @param recipeInfoEntity 연결할 레시피 정보 엔티티
     * @return List<RecipeIngredientEntity>
     */
    public List<RecipeIngredientEntity> toEntityList(List<RecipeIngredientEntityDto> dtos, 
                                                    RecipeInfoEntity recipeInfoEntity) {
        if (dtos == null) {
            return null;
        }
        
        List<RecipeIngredientEntity> ingredientEntities = dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        
        // 양방향 관계가 있는 경우 설정
        if (recipeInfoEntity != null) {
            ingredientEntities.forEach(ingredient -> ingredient.setRecipeInfoEntity(recipeInfoEntity));
        }
        
        return ingredientEntities;
    }
}

package com.inmyhand.refrigerator.recipe.mapper;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeNutrientAnalysisMapper {

    /**
     * DTO -> RecipeNutrientAnalysisEntity
     * (RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dto 영양소 분석 DTO
     * @return RecipeNutrientAnalysisEntity
     */
    public RecipeNutrientAnalysisEntity toEntity(RecipeNutrientAnalysisEntityDto dto) {
        if (dto == null) {
            return null;
        }

        RecipeNutrientAnalysisEntity entity = new RecipeNutrientAnalysisEntity();
        entity.setId(dto.getId());
        entity.setAnalysisResult(dto.getAnalysisResult());
        entity.setScore(dto.getFitnessScore());
        entity.setCarbs(dto.getCarbohydrate());
        entity.setProtein(dto.getProtein());
        entity.setFat(dto.getFat());
        entity.setMineral(dto.getMineral());
        entity.setVitamin(dto.getVitamin());

        return entity;
    }

    public RecipeNutrientAnalysisEntity toEntity(
            RecipeNutrientAnalysisEntityDto dto,
            RecipeInfoEntity recipe,
            MemberEntity member
    ) {
        if (dto == null || recipe == null || member == null) {
            return null;
        }

        RecipeNutrientAnalysisEntity entity = toEntity(dto);
        entity.setRecipeInfoEntity(recipe);
        entity.setMemberEntity(member);
        return entity;
    }

    /**
     * RecipeNutrientAnalysisEntity -> DTO
     *
     * @param entity 영양소 분석 엔티티
     * @return RecipeNutrientAnalysisEntityDto
     */
    public RecipeNutrientAnalysisEntityDto toDto(RecipeNutrientAnalysisEntity entity) {
        if (entity == null) {
            return null;
        }
        
        RecipeNutrientAnalysisEntityDto dto = new RecipeNutrientAnalysisEntityDto();
        dto.setId(entity.getId());
        dto.setAnalysisResult(entity.getAnalysisResult());
        dto.setFitnessScore(entity.getScore());
        dto.setCarbohydrate(entity.getCarbs());
        dto.setProtein(entity.getProtein());
        dto.setFat(entity.getFat());
        dto.setMineral(entity.getMineral());
        dto.setVitamin(entity.getVitamin());
        
        return dto;
    }

    /**
     * List<RecipeNutrientAnalysisEntity> -> List<DTO>
     *
     * @param entities 영양소 분석 엔티티 목록
     * @return List<RecipeNutrientAnalysisEntityDto>
     */
    public List<RecipeNutrientAnalysisEntityDto> toDtoList(List<RecipeNutrientAnalysisEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * List<DTO> -> List<RecipeNutrientAnalysisEntity>
     * (RecipeInfoEntity는 별도로 설정해야 함)
     *
     * @param dtos 영양소 분석 DTO 목록
     * @return List<RecipeNutrientAnalysisEntity>
     */
    public List<RecipeNutrientAnalysisEntity> toEntityList(List<RecipeNutrientAnalysisEntityDto> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

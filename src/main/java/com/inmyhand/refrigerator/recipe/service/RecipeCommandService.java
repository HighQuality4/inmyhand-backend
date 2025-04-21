package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCategoryEntityDto;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeIngredientEntityDto;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRequestDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeStepsEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCategoryEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity;
import com.inmyhand.refrigerator.recipe.domain.enums.CookingTimeEnum;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

// 레시피 생성, 수정, 삭제 담당
@Service
@RequiredArgsConstructor
public class RecipeCommandService {

    @Autowired
    private final RecipeInfoRepository infoRepository;
    @Autowired
    private final MemberRepository memberRepository;

    // 레시피 생성
    @Transactional
    public void createRecipe(RecipeRequestDTO dto) {

        MemberEntity member = memberRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        RecipeInfoEntity recipe = RecipeInfoEntity.builder()
                .recipeName(dto.getRecipeName())
                .difficulty(dto.getDifficulty())
                .cookingTime(CookingTimeEnum.fromLabel(dto.getCookingTime()))
                .calories(dto.getCalories())
                .summary(dto.getSummary())
                .servings(dto.getServings())
                .recipeDepth(1)
                .memberEntity(member)
                .build();

        // 파일 추가
        if (dto.getFiles() != null) {
            for (String url : dto.getFiles()) {
                FilesEntity file = new FilesEntity();
                file.setFileUrl(url);
                file.setRecipeInfoEntity(recipe);
                recipe.addFilesEntity(file);
            }
        }

        // 단계 추가
        if (dto.getSteps() != null) {
            for (RecipeStepsEntityDto stepDto : dto.getSteps()) {
                RecipeStepsEntity step = new RecipeStepsEntity();
                step.setStepNumber(stepDto.getStepNumber());
                step.setStepDescription(stepDto.getStepDescription());
                step.setRecipeInfoEntity(recipe);

                if (stepDto.getFileUrl() != null && !stepDto.getFileUrl().isBlank()) {
                    FilesEntity stepFile = new FilesEntity();
                    stepFile.setFileUrl(stepDto.getFileUrl());
                    stepFile.setRecipeStepEntity(step);
                    step.setFilesEntity(stepFile);
                }
                recipe.addRecipeStep(step);
            }
        }

        // 재료 추가
        if (dto.getIngredients() != null) {
            for (RecipeIngredientEntityDto ing : dto.getIngredients()) {
                RecipeIngredientEntity ingredient = new RecipeIngredientEntity();
                ingredient.setIngredientName(ing.getIngredientName());
                ingredient.setIngredientGroup(ing.getIngredientGroup());
                ingredient.setIngredientQuantity(ing.getIngredientQuantity());
                ingredient.setIngredientUnit(ing.getIngredientUnit());
                recipe.addRecipeIngredient(ingredient);
            }
        }

        // 카테고리 추가
        if (dto.getCategories() != null) {
            for (RecipeCategoryEntityDto cat : dto.getCategories()) {
                RecipeCategoryEntity category = new RecipeCategoryEntity();
                category.setRecipeCategoryName(cat.getRecipeCategoryName());
                category.setRecipeCategoryType(cat.getRecipeCategoryType());
                recipe.addRecipeCategory(category);
            }
        }

        infoRepository.save(recipe);
    }
    // 레시피 수정
    @Transactional
    public void updateRecipe(Long recipeId, RecipeRequestDTO dto) {
        RecipeInfoEntity recipe = infoRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

        // 작성자 확인
        if (!recipe.getMemberEntity().getId().equals(dto.getUserId())) {
            throw new RuntimeException("레시피 수정 권한이 없습니다.");
        }

        // 기본 정보 수정
        recipe.setRecipeName(dto.getRecipeName());
        recipe.setDifficulty(dto.getDifficulty());
        recipe.setCookingTime(CookingTimeEnum.fromLabel(dto.getCookingTime()));
        recipe.setCalories(dto.getCalories());
        recipe.setSummary(dto.getSummary());
        recipe.setServings(dto.getServings());
        recipe.setUpdatedAt(new Date());

        // 기존 하위 엔티티 초기화 (clear + orphanRemoval)
        recipe.getRecipeStepsList().clear();
        recipe.getRecipeIngredientList().clear();
        recipe.getRecipeCategoryList().clear();
        recipe.getFilesEntities().clear();

        // 파일 다시 추가 (레시피 대표 이미지 등)
        if (dto.getFiles() != null) {
            for (String url : dto.getFiles()) {
                FilesEntity file = new FilesEntity();
                file.setFileUrl(url);
                file.setRecipeInfoEntity(recipe); // 연관관계 설정
                recipe.addFilesEntity(file);
            }
        }

        // 단계 다시 추가
        if (dto.getSteps() != null) {
            for (RecipeStepsEntityDto stepDto : dto.getSteps()) {
                RecipeStepsEntity step = new RecipeStepsEntity();
                step.setStepNumber(stepDto.getStepNumber());
                step.setStepDescription(stepDto.getStepDescription());
                step.setRecipeInfoEntity(recipe);

                if (stepDto.getFileUrl() != null && !stepDto.getFileUrl().isBlank()) {
                    FilesEntity stepFile = new FilesEntity();
                    stepFile.setFileUrl(stepDto.getFileUrl());
                    stepFile.setRecipeStepEntity(step); // 연관관계 설정
                    step.setFilesEntity(stepFile);
                }

                recipe.addRecipeStep(step);
            }
        }

        // 재료 다시 추가
        if (dto.getIngredients() != null) {
            for (RecipeIngredientEntityDto ing : dto.getIngredients()) {
                RecipeIngredientEntity ingredient = new RecipeIngredientEntity();
                ingredient.setIngredientName(ing.getIngredientName());
                ingredient.setIngredientGroup(ing.getIngredientGroup());
                ingredient.setIngredientQuantity(ing.getIngredientQuantity());
                ingredient.setIngredientUnit(ing.getIngredientUnit());
                ingredient.setRecipeInfoEntity(recipe);
                recipe.addRecipeIngredient(ingredient);
            }
        }

        // 카테고리 다시 추가
        if (dto.getCategories() != null) {
            for (RecipeCategoryEntityDto cat : dto.getCategories()) {
                RecipeCategoryEntity category = new RecipeCategoryEntity();
                category.setRecipeCategoryName(cat.getRecipeCategoryName());
                category.setRecipeCategoryType(cat.getRecipeCategoryType());
                category.setRecipeInfoEntity(recipe);
                recipe.addRecipeCategory(category);
            }
        }

        infoRepository.save(recipe);
    }


    // 레시피 삭제
    @Transactional
    public void deleteRecipe(Long recipeId
            //, Long userId
    ) {
        RecipeInfoEntity recipe = infoRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

        // 작성자 확인
//        if (!recipe.getMemberEntity().getId().equals(userId)) {
//            throw new RuntimeException("레시피 삭제 권한이 없습니다.");
//        }

        infoRepository.delete(recipe);
    }

}

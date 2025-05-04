package com.inmyhand.refrigerator.recipe.service;

import com.inmyhand.refrigerator.files.domain.entity.FileUploadRequest;
import com.inmyhand.refrigerator.files.domain.entity.FileUploadResponse;
import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import com.inmyhand.refrigerator.files.repository.FilesRepository;
import com.inmyhand.refrigerator.files.service.FileUploadService;
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
import com.inmyhand.refrigerator.recipe.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

// 레시피 생성, 수정, 삭제 담당
@Service
@RequiredArgsConstructor
public class RecipeCommandService {

    private final RecipeInfoRepository infoRepository;
    private final RecipeStepRepository stepRepository;
    private final MemberRepository memberRepository;
    private final FilesRepository filesRepository;
    private final FileUploadService fileUploadService;

    // 레시피 생성
    @Transactional
    public void createRecipe(RecipeRequestDTO dto, MultipartFile file, List<MultipartFile>stepFiles) {
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

        RecipeInfoEntity savedRecipe = infoRepository.save(recipe);

        // 대표 이미지 저장
        if (file != null && !file.isEmpty()) {
            FileUploadRequest uploadReq = new FileUploadRequest(file, null, savedRecipe.getId(), null);
            FileUploadResponse res = fileUploadService.uploadByType(uploadReq);

            FilesEntity fileEntity = filesRepository.findByFileUrl(res.getFileUrl())
                    .orElseThrow(() -> new RuntimeException("대표 이미지 저장 실패"));

            fileEntity.setRecipeInfoEntity(savedRecipe); // 연관 설정만
            savedRecipe.addFilesEntity(fileEntity);
        }

        // 단계 추가
        if (dto.getSteps() != null) {
            for (int i = 0; i < dto.getSteps().size(); i++) {
                RecipeStepsEntityDto stepDto = dto.getSteps().get(i);
                RecipeStepsEntity step = new RecipeStepsEntity();
                step.setStepNumber(stepDto.getStepNumber());
                step.setStepDescription(stepDto.getStepDescription());
                step.setRecipeInfoEntity(savedRecipe);

                RecipeStepsEntity savedStep = stepRepository.save(step);

                // 단계 이미지 저장
                if (stepFiles != null && i < stepFiles.size()) {
                    MultipartFile stepFile = stepFiles.get(i);
                    if (!stepFile.isEmpty()) {
                        FileUploadRequest uploadReq = new FileUploadRequest(stepFile, null, null, savedStep.getId());
                        FileUploadResponse res = fileUploadService.uploadByType(uploadReq);

                        FilesEntity stepImg = filesRepository.findByFileUrl(res.getFileUrl())
                                .orElseThrow(() -> new RuntimeException("단계 이미지 저장 실패"));

                        stepImg.setRecipeStepEntity(savedStep);
                        savedStep.setFilesEntity(stepImg);
                    }
                }
                savedRecipe.addRecipeStep(savedStep);
            }
        }
    }

    // 레시피 수정
    @Transactional
    public void updateRecipe(Long recipeId, RecipeRequestDTO dto, MultipartFile file, List<MultipartFile> stepFiles) {
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

        // 기존 하위 엔티티 제거 (orphanRemoval)
        recipe.getRecipeStepsList().clear();
        recipe.getRecipeIngredientList().clear();
        recipe.getRecipeCategoryList().clear();
        recipe.getFilesEntities().clear();

        // 대표 이미지 수정
        if (file != null && !file.isEmpty()) {
            FileUploadRequest uploadReq = new FileUploadRequest(file, null, recipeId, null);
            FileUploadResponse res = fileUploadService.uploadByType(uploadReq);

            FilesEntity fileEntity = filesRepository.findByFileUrl(res.getFileUrl())
                    .orElseThrow(() -> new RuntimeException("대표 이미지 저장 실패"));
            fileEntity.setRecipeInfoEntity(recipe);
            recipe.addFilesEntity(fileEntity);
        }

        // 단계 수정
        if (dto.getSteps() != null) {
            for (int i = 0; i < dto.getSteps().size(); i++) {
                RecipeStepsEntityDto stepDto = dto.getSteps().get(i);
                RecipeStepsEntity step = new RecipeStepsEntity();
                step.setStepNumber(stepDto.getStepNumber());
                step.setStepDescription(stepDto.getStepDescription());
                step.setRecipeInfoEntity(recipe);

                RecipeStepsEntity savedStep = stepRepository.save(step);

                if (stepFiles != null && i < stepFiles.size()) {
                    MultipartFile stepFile = stepFiles.get(i);
                    if (!stepFile.isEmpty()) {
                        FileUploadRequest uploadReq = new FileUploadRequest(stepFile, null, null, savedStep.getId());
                        FileUploadResponse res = fileUploadService.uploadByType(uploadReq);

                        FilesEntity stepImg = filesRepository.findByFileUrl(res.getFileUrl())
                                .orElseThrow(() -> new RuntimeException("단계 이미지 저장 실패"));

                        stepImg.setRecipeStepEntity(savedStep);
                        savedStep.setFilesEntity(stepImg);
                    }
                }

                recipe.addRecipeStep(savedStep);
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

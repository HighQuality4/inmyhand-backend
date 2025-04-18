//package com.inmyhand.refrigerator.recipe.service;
//
//import com.inmyhand.refrigerator.recipe.domain.dto.*;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeNutrientAnalysisEntity;
//import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
//import com.inmyhand.refrigerator.recipe.repository.RecipeIngredientRepository;
//import com.inmyhand.refrigerator.recipe.repository.RecipeStepRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//// 조인 기반 상세 조회, 리스트 조회 전용
//@Service
//@RequiredArgsConstructor
//public class RecipeQueryServiceImpl implements RecipeQueryService {
//    @Autowired
//    private RecipeInfoRepository infoRepository;
//    @Autowired
//    private RecipeIngredientRepository ingredientRepository;
//    @Autowired
//    private RecipeStepRepository stepRepository;
//
//    //  List<RecipeInfoEntity>에서 List<RecipeSummaryDTO> 로 변환
//    public List<RecipeSummaryDTO> convertToRecipeSummaryList(List<RecipeInfoEntity> recipes) {
//        return recipes.stream()
//                .map(recipe -> {
//                    List<RecipeCategoryDTO> categories = recipe.getRecipeCategoryList().stream()
//                            .map(c -> new RecipeCategoryDTO(
//                                    c.getRecipeCategoryName(),
//                                    c.getRecipeCategoryType()))
//                            .collect(Collectors.toList());
//
//                    List<String> fileUrls = recipe.getFilesEntities().stream()
//                            .map(file -> file.getFileUrl())
//                            .collect(Collectors.toList());
//
//                    return new RecipeSummaryDTO(
//                            fileUrls,
//                            categories,
//                            recipe.getRecipeName(),
//                            recipe.getDifficulty(),
//                            recipe.getCookingTime().getLabel(),  // Enum → String 변환
//                            recipe.getCalories(),
//                            (long) recipe.getRecipeLikesList().size(),
//                            recipe.getMemberEntity().getNickname()
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//    // 모든 레시피 목록 조회
//    public List<RecipeSummaryDTO> getAllRecipeList() {
//        List<RecipeInfoEntity> recipes = infoRepository.findAllByOrderByCreatedAtDesc();
//
//        return convertToRecipeSummaryList(recipes);
//    }
//
//    // 인기 레시피 목록 조회
//    public List<RecipeSummaryDTO> getPopularRecipeList() {
//        List<RecipeInfoEntity> recipes = infoRepository.findTop5ByOrderByLikesCountDesc(PageRequest.of(0, 5));
//
//        return convertToRecipeSummaryList(recipes);
//    }
//
//    // 레시피 정렬 (난이도, 소요시간, 칼로리 오름차순/내림차순)
//    public List<RecipeSummaryDTO> getArrayRecipeList(String orderBy, String sortType) {
//        Sort.Direction direction = Sort.Direction.fromString(sortType.toUpperCase());
//
//        Sort sort = Sort.by(direction, orderBy);
//        List<RecipeInfoEntity> recipes = infoRepository.findAll(sort);
//
//        return convertToRecipeSummaryList(recipes);
//    }
//
//    // 레시피 상세
//    public RecipeDetailDTO getRecipeDetail(Long recipeId) {
//        RecipeInfoEntity recipe = infoRepository.findById(recipeId)
//                .orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다."));
//
//        List<RecipeCategoryDTO> categories = recipe.getRecipeCategoryList().stream()
//                .map(c -> new RecipeCategoryDTO(
//                        c.getRecipeCategoryName(),
//                        c.getRecipeCategoryType()))
//                .collect(Collectors.toList());
//
//        List<String> fileUrls = recipe.getFilesEntities().stream()
//                .map(file -> file.getFileUrl())
//                .collect(Collectors.toList());
//
//        List<RecipeIngredientDTO> ingredients = recipe.getRecipeIngredientList().stream()
//                .map(i -> new RecipeIngredientDTO(
//                        i.getIngredientGroup(),
//                        i.getIngredientName(),
//                        i.getIngredientQuantity(),
//                        i.getIngredientUnit())
//                )
//                .collect(Collectors.toList());
//
//        List<RecipeStepDTO> steps = recipe.getRecipeStepsList().stream()
//                .map(s -> new RecipeStepDTO(
//                        s.getStepNumber(),
//                        s.getStepDescription(),
//                        s.getFilesEntity().getFileUrl()
//                        )
//                )
//                .collect(Collectors.toList());
//
//        List<RecipeCommentDTO> comments = recipe.getRecipeCommentList().stream()
//                .map(c -> new RecipeCommentDTO(
//                                c.getMemberEntity().getNickname(),
//                                c.getCommentContents(),
//                                c.getCreatedAt()
//                        )
//                )
//                .collect(Collectors.toList());
//
//        RecipeNutrientAnalysisEntity analysisEntity = recipe.getRecipeNutrientAnalysisList()
//                .stream()
//                .findFirst()
//                .orElse(null);
//
//        RecipeNutrientAnalysisDTO analysis = null;
//        if (analysisEntity != null) {
//            analysis = new RecipeNutrientAnalysisDTO(
//                    analysisEntity.getAnalysisResult(),
//                    analysisEntity.getScore(),
//                    analysisEntity.getCarbs(),
//                    analysisEntity.getProtein(),
//                    analysisEntity.getFat(),
//                    analysisEntity.getMineral(),
//                    analysisEntity.getVitamin()
//            );
//        }
//
//        return new RecipeDetailDTO(
//                recipe.getId(),
//                recipe.getRecipeName(),
//                categories,
//                fileUrls,
//                recipe.getServings(),
//                recipe.getDifficulty(),
//                recipe.getCookingTime().getLabel(),
//                recipe.getCalories(),
//                recipe.getMemberEntity().getNickname(),
//                (long) recipe.getRecipeLikesList().size(),
//                (long) recipe.getRecipeViewsList().size(),
//                recipe.getSummary(),
//                recipe.getCreatedAt(),
//                recipe.getUpdatedAt(),
//                ingredients,
//                steps,
//                analysis,
//                comments
//        );
//    }
//}

//package com.inmyhand.refrigerator.recipe.domain.recipeparsing;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
//import com.inmyhand.refrigerator.files.repository.FilesRepository;
//import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
//import com.inmyhand.refrigerator.member.repository.MemberRepository;
//import com.inmyhand.refrigerator.recipe.domain.dto2.RecipeDetailDTO;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCategoryEntity;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeIngredientEntity;
//import com.inmyhand.refrigerator.recipe.domain.entity.RecipeStepsEntity;
//import com.inmyhand.refrigerator.recipe.domain.enums.CategoryTypeEnum;
//import com.inmyhand.refrigerator.recipe.domain.enums.CookingTimeEnum;
//import com.inmyhand.refrigerator.recipe.domain.enums.DifficultyEnum;
//import com.inmyhand.refrigerator.recipe.mapper.RecipeDetailMapper;
//import com.inmyhand.refrigerator.recipe.repository.RecipeCategoryRepository;
//import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
//import com.inmyhand.refrigerator.recipe.repository.RecipeIngredientRepository;
//import com.inmyhand.refrigerator.recipe.repository.RecipeStepRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//
//@SpringBootTest
//@Rollback(false)
//@Transactional
//class RecipeServicePasTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    RecipeInfoRepository recipeInfoRepository;
//    @Autowired
//    RecipeCategoryRepository recipeCategoryRepository;
//    @Autowired
//    RecipeStepRepository recipeStepRepository;
//    @Autowired
//    FilesRepository filesRepository;
//    @Autowired
//    RecipeIngredientRepository recipeIngredientRepository;
//
//    @Autowired
//    RecipeDetailMapper recipeDetailMapper;
//
//
//    private String readJsonFile() throws IOException {
//        ClassPathResource resource = new ClassPathResource("recipes.json");
//        byte[] byteData = FileCopyUtils.copyToByteArray(resource.getInputStream());
//        return new String(byteData, StandardCharsets.UTF_8);
//    }
//
//    private List<Recipe> convertJsonToObjects(String jsonContent) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(jsonContent, new TypeReference<List<Recipe>>() {
//        });
//    }
//
//
//    @Test
//    void init() throws IOException {
//
//        String s = readJsonFile();
//        List<Recipe> recipes = convertJsonToObjects(s);
//
//        MemberEntity fm = null;
//        //given
//
//        try {
//            fm = memberRepository.findById(1L)
//                    .orElseThrow(() -> new RuntimeException("Member not found"));
//        } catch (RuntimeException e) {
//            fm = new MemberEntity();
//            fm.setNickname("ADMIN");
//            fm.setMemberName("ADMIN");
//            fm.setEmail("admin@inmyhand.com");
//            fm.setProviderId("LOCAL");
//            memberRepository.save(fm);
//        }
//
////        for (int i = 0; i < 11; i++) {
//        //------------------------------
//        int i = 0;
//        //------------------------------
//
//
//
//        if (recipes.get(i).getManual01().isBlank()) {
//            return;
//        }
//
//        System.out.println("recipes.get(i).getManual04() = " + recipes.get(i).getManual04());
//
//        RecipeInfoEntity recipeInfoEntity = RecipeInfoEntity.builder()
//                .recipeName(recipes.get(i).getRcpNm())
//                .difficulty(DifficultyEnum.보통)
//                .cookingTime(CookingTimeEnum.FROM_10_TO_30_MINUTES)
//                .calories((int) Math.round(recipes.get(i).getInfoEng()))
//                .recipeDepth(1)
//                .servings(1)
//                .memberEntity(fm)
//                .build();
//
//        RecipeInfoEntity recipeInfo = recipeInfoRepository.save(recipeInfoEntity);
//
//
//        // step
//        List<RecipeStepsEntity> recipeSteps = new ArrayList<>();
//        RecipeStepsEntity recipeStepsEntity = null;
//
//        for (int j = 0; j < 6; j++) {
//            recipeStepsEntity = new RecipeStepsEntity();
//            recipeStepsEntity.setStepNumber(j + 1);
//            FilesEntity files = null; // 각 단계마다 files 객체 초기화
//
//            // 단계 설명 설정
//            if (!recipes.get(i).getManual01().isBlank() && j == 0) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual01().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            } else if (!recipes.get(i).getManual02().isBlank() && j == 1) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual02().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            } else if (!recipes.get(i).getManual03().isBlank() && j == 2) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual03().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            } else if (!recipes.get(i).getManual04().isBlank() && j == 3) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual04().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            } else if (!recipes.get(i).getManual05().isBlank() && j == 4) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual05().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            } else if (!recipes.get(i).getManual06().isBlank() && j == 5) {
//                recipeStepsEntity.setStepDescription(recipes.get(i).getManual06().replaceAll("^\\d+\\.\\s*", "")
//                        .replaceAll("\\n", " "));
//            }
//
//            // 단계 설명이 있는 경우에만 파일 처리 및 저장
//            if (StringUtils.hasText(recipeStepsEntity.getStepDescription())) {
//                // 이미지 설정
//                if (!recipes.get(i).getManualImg01().isBlank() && j == 0) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg01())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg01())
//                            .build();
//                } else if (!recipes.get(i).getManualImg02().isBlank() && j == 1) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg02())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg02())
//                            .build();
//                } else if (!recipes.get(i).getManualImg03().isBlank() && j == 2) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg03())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg03())
//                            .build();
//                } else if (!recipes.get(i).getManualImg04().isBlank() && j == 3) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg04())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg04())
//                            .build();
//                } else if (!recipes.get(i).getManualImg05().isBlank() && j == 4) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg05())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg05())
//                            .build();
//                } else if (!recipes.get(i).getManualImg06().isBlank() && j == 5) {
//                    files = FilesEntity.builder()
//                            .orgName(recipes.get(i).getManualImg06())
//                            .sysName(UUID.randomUUID().toString())
//                            .fileUrl(recipes.get(i).getManualImg06())
//                            .build();
//                }
//
//                // 단계 저장
//                recipeStepsEntity.setRecipeInfoEntity(recipeInfoEntity);
//                RecipeStepsEntity save = recipeStepRepository.save(recipeStepsEntity);
//
//                // 파일이 있는 경우만 파일 저장
//                if (files != null) {
//                    recipeStepsEntity.setFilesEntity(files);
//                    files.setRecipeStepEntity(save);
//                    filesRepository.save(files);
//                }
//
//                recipeSteps.add(save);
//            }
//        }
//
//
//
//        // files
//        List<FilesEntity> filesEntities = new ArrayList<>();
//
//        FilesEntity filesEntity = FilesEntity.builder()
//                .orgName(recipes.get(i).getAttFileNoMain())
//                .sysName(UUID.randomUUID().toString())
//                .fileUrl(recipes.get(i).getAttFileNoMain())
//                .build();
//        filesEntity.setRecipeInfoEntity(recipeInfo);
//        filesEntities.add(filesEntity);
//
//        filesRepository.saveAll(filesEntities);
//
//        //category
//        List<RecipeCategoryEntity> categoryEntities = new ArrayList<>();
//        RecipeCategoryEntity categoryEntity = new RecipeCategoryEntity();
//        categoryEntity.setRecipeInfoEntity(recipeInfoEntity);
//        categoryEntity.setRecipeCategoryName(recipes.get(i).getRcpWay2());
//        categoryEntity.setRecipeCategoryType(CategoryTypeEnum.방법별);
//        categoryEntities.add(categoryEntity);
//
//        recipeCategoryRepository.saveAll(categoryEntities);
//
//
//        List<RecipeIngredientEntity> recipeIngredientEntities = Arrays.asList(
//                new RecipeIngredientEntity("소고기", 120, "g", recipeInfo),
//                new RecipeIngredientEntity("달걀", 1, "개", recipeInfo),
//                new RecipeIngredientEntity("닭고기", 100, "g", recipeInfo),
//                new RecipeIngredientEntity("미나리", 50, "g", recipeInfo),
//                new RecipeIngredientEntity("찹쌀가루", 50, "g", recipeInfo),
//                new RecipeIngredientEntity("홍고추", 1, "개", recipeInfo),
//                new RecipeIngredientEntity("소금", 0.3, "g", recipeInfo),
//                new RecipeIngredientEntity("후춧가루", 0.01, "g", recipeInfo),
//                new RecipeIngredientEntity("통후추", 5, "알", recipeInfo),
//                new RecipeIngredientEntity("저염간장", 20, "g", recipeInfo),
//                new RecipeIngredientEntity("설탕", 20, "g", recipeInfo),
//                new RecipeIngredientEntity("식초", 10, "g", recipeInfo)
//
//
//        );
//
//
//        List<RecipeIngredientEntity> recipeIngredientEntities1 = recipeIngredientRepository.saveAll(recipeIngredientEntities);
//
//        recipeInfo.setRecipeIngredientList(recipeIngredientEntities1);
//        recipeInfo.setRecipeCategoryList(categoryEntities);
//        recipeInfo.setRecipeStepsList(recipeSteps);
//        recipeInfo.setFilesEntities(filesEntities);
//        recipeInfo.setRecipeCommentList(null);
//        recipeInfo.setRecipeLikesList(null);
//        recipeInfo.setRecipeViewsList(null);
//        recipeInfo.setRecipeNutrientAnalysisList(null) ;
//
////        List<RecipeDetailDTO> dtoList = recipeDetailMapper.toDtoList(recipeInfoRepository.findAll());
////        System.out.println("-------------");
////        System.out.println("dtoList = " + dtoList);
////        System.out.println("-------------");
//
//    }
//}
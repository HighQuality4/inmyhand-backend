package com.inmyhand.refrigerator.recipe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.common.redis.search.PopularSearchService;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRequestDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeCommandService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import com.inmyhand.refrigerator.recipe.service.engine.SimilarRecipeLogic;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeQueryService recipeQueryService;
    private final RecipeCommandService recipeCommandService;
    private final ObjectMapper objectMapper;
    private final SimilarRecipeLogic similarRecipeLogic;
    private final PopularSearchService popularSearchService;

    // 전체 레시피 목록 조회 - 페이징
    @PostMapping
    public Page<RecipeSummaryDTO> getAllRecipeList(@RequestBody Map<String, Object> body) {
        Map<String, Object> param = (Map<String, Object>) body.get("param");

        int page = 0;
        int size = 6;

        if (param != null) {
            if (param.get("page") instanceof List<?> pageList && !pageList.isEmpty()) {
                page = Integer.parseInt(pageList.get(0).toString());
            }
            if (param.get("size") instanceof List<?> sizeList && !sizeList.isEmpty()) {
                size = Integer.parseInt(sizeList.get(0).toString());
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        return recipeQueryService.getAllRecipeList(pageable);
    }

    // 인기 레시피 목록 조회
    @PostMapping("/popular")
        public List<RecipeSummaryDTO> getPopularRecipeList() {
        return recipeQueryService.getPopularRecipeList();
    }

    // 레시피 목록 정렬 조회 (난이도, 칼로리) - 페이징
    @PostMapping("/sort")
    public Page<RecipeSummaryDTO> getSortRecipeList(@RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                    @RequestParam(name = "sortType", required = false, defaultValue = "ASC") String sortType,
                                                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "6") int size) {
        return recipeQueryService.getArrayRecipeList(sortBy, sortType, page, size);
    }

    // 레시피 상세 조회
    @PostMapping("/{id}")
    public RecipeDetailDTO getRecipeDetail(@PathVariable("id") Long recipeId) {
        return recipeQueryService.getRecipeDetail(recipeId);
    }

    // 추천 레시피 3개
    @PostMapping("/similar/{id}")
    public ResponseEntity<?> getSimilarRecipes(@PathVariable("id") Long recipeId) {
        return ResponseEntity.ok(Map.of("similar", similarRecipeLogic.getSimilarRecipes(recipeId)));
    }

    // 레시피 검색
    @PostMapping("/search")
    public Page<RecipeSummaryDTO> getSearchRecipeList(@RequestParam("keyword") String keyword,
                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                      @RequestParam(name = "size", required = false, defaultValue = "6") int size) {
//        System.out.println(size);
        popularSearchService.registerSearchKeyword(keyword);
        return recipeQueryService.getSearchRecipeList(keyword, page, size);
    }

    // 레시피 생성
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createRecipe(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestPart("param") String body,
                                             @RequestPart(value = "files", required = false) MultipartFile files,
                                             @RequestPart(value = "stepFiles", required = false) List<MultipartFile> stepFiles)
    {
        try { // JSON 문자열을 Map으로 파싱
            Map<String, Object> parsedBody = objectMapper.readValue(body, new TypeReference<>() {});
            List<Map<String, Object>> paramList = (List<Map<String, Object>>) parsedBody.get("param");
            Map<String, Object> recipeMap = paramList.get(0);
            RecipeRequestDTO dto = objectMapper.convertValue(recipeMap, RecipeRequestDTO.class);
            dto.setUserId(userDetails.getUserId());
            recipeCommandService.createRecipe(dto, files, stepFiles);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 레시피 수정
    @PutMapping("/{recipeId}")
    public ResponseEntity<String> updateRecipe(
            @PathVariable("recipeId") Long recipeId,
            @RequestPart("param") String body,
            @RequestPart(value = "files", required = false) MultipartFile files,
            @RequestPart(value = "stepFiles", required = false) List<MultipartFile> stepFiles) {
        try { // JSON 문자열을 Map으로 파싱
            Map<String, Object> parsedBody = objectMapper.readValue(body, new TypeReference<>() {});
            List<Map<String, Object>> paramList = (List<Map<String, Object>>) parsedBody.get("param");
            Map<String, Object> recipeMap = paramList.get(0);
            RecipeRequestDTO dto = objectMapper.convertValue(recipeMap, RecipeRequestDTO.class);
            recipeCommandService.updateRecipe(recipeId, dto, files, stepFiles);
            return ResponseEntity.ok("레시피가 성공적으로 수정되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 레시피 삭제
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable("recipeId") Long recipeId
           //@RequestParam Long userId
    ) {
        recipeCommandService.deleteRecipe(recipeId);
        return ResponseEntity.ok("레시피가 성공적으로 삭제되었습니다.");
    }
}
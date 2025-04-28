package com.inmyhand.refrigerator.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRequestDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.SimilarRecipeDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeCommandService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import com.inmyhand.refrigerator.recipe.service.engine.SimilarRecipeLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/search")
    public List<RecipeSummaryDTO> getSearchRecipeList(@RequestParam("keyword") String keyword) {
        return recipeQueryService.getSearchRecipeList(keyword);
    }

    // 레시피 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createRecipe(@RequestBody Map<String, Object> body) {
        Map<String, Object> param = (Map<String, Object>) body.get("param");
        List<Map<String, Object>> paramList = (List<Map<String, Object>>) param.get("param");

        Map<String, Object> recipeMap = paramList.get(0);

        RecipeRequestDTO dto = objectMapper.convertValue(recipeMap, RecipeRequestDTO.class);

        recipeCommandService.createRecipe(dto);
        return ResponseEntity.ok().build();
    }

    // 레시피 수정
    @PutMapping("/{recipeId}")
    public ResponseEntity<String> updateRecipe(
            @PathVariable Long recipeId,
            @RequestBody RecipeRequestDTO dto) {
        recipeCommandService.updateRecipe(recipeId, dto);
        return ResponseEntity.ok("레시피가 성공적으로 수정되었습니다.");
    }

    // 레시피 삭제
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(
            @PathVariable Long recipeId
           //@RequestParam Long userId
    ) {
        recipeCommandService.deleteRecipe(recipeId);
        return ResponseEntity.ok("레시피가 성공적으로 삭제되었습니다.");
    }
}
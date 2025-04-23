package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRequestDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeCommandService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
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
public class RecipeController {
    @Autowired
    private RecipeQueryService recipeQueryService;
    @Autowired
    private RecipeCommandService recipeCommandService;

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

    // 레시피 목록 정렬 조회 (난이도, 소요시간, 칼로리)
    @GetMapping("/sort")
    public List<RecipeSummaryDTO> getSortRecipeList(@RequestParam(name = "sortBy", required = false) String sortBy,
                                                  @RequestParam(name = "type", required = false, defaultValue = "ASC") String type) {
        return recipeQueryService.getArrayRecipeList(sortBy, type);
    }

    // 레시피 상세 조회
    @GetMapping("/{id}")
    public RecipeDetailDTO getRecipeDetail(@PathVariable("id") Long recipeId) {
        return recipeQueryService.getRecipeDetail(recipeId);
    }

    // 레시피 검색
    @GetMapping("/search")
    public List<RecipeSummaryDTO> getSearchRecipeList(@RequestParam("keyword") String keyword) {
        return recipeQueryService.getSearchRecipeList(keyword);
    }

    // 레시피 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createRecipe(@RequestBody RecipeRequestDTO dto){
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
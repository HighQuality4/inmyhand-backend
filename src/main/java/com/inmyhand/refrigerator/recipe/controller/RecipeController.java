package com.inmyhand.refrigerator.recipe.controller;

import com.inmyhand.refrigerator.recipe.domain.dto.RecipeDetailDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeRequestDTO;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeSummaryDTO;
import com.inmyhand.refrigerator.recipe.service.RecipeCommandService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeQueryService recipeQueryService;
    @Autowired
    private RecipeCommandService recipeCommandService;

    // 전체 레시피 목록 조회
    @GetMapping
    public List<RecipeSummaryDTO> getAllRecipeList() {
        return recipeQueryService.getAllRecipeList();
    }

    // 인기 레시피 목록 조회
    @GetMapping("/popular")
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
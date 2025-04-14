package com.inmyhand.refrigerator.category.controller;

import com.inmyhand.refrigerator.category.EmbeddingUtil;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.service.FoodVectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodVectorController {

    private final FoodVectorService foodVectorService;
    private final EmbeddingUtil embeddingUtil;

    @GetMapping("/embedding")
    public List<Float[]> getEmbedding(@RequestParam List<String> text) {
        return embeddingUtil.getEmbedding(text);
    }

//    @PostMapping("save")
//    public ResponseEntity<String> save(@RequestBody FoodVectorRequest request) {
//        foodVectorService.saveFoodVector(request.getNaturalText(), request.getCategoryName());
//        return ResponseEntity.ok("Success");
//    }

    @PostMapping("/search")
    public List<FoodVectorRequestDTO> search(@RequestBody List<String> input) {
        return foodVectorService.findSimilarCategories(input);
    }
}

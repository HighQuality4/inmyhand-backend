package com.inmyhand.refrigerator.category.controller;

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

    @PostMapping("/search")
    public List<FoodVectorRequestDTO> search(@RequestBody List<String> input) {
        return foodVectorService.findSimilarCategories(input);
    }
}

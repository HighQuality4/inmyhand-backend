package com.inmyhand.refrigerator.category.controller;

import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.service.FoodVectorService;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.util.ParserJsonStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodVectorController {

    private final FoodVectorService foodVectorService;
    private final ParserJsonStringUtil parserJsonStringUtil;

//    @PostMapping("/search")
//    public List<ReceiptDTO> search(List<MultipartFile> files) {
//
//
//        List<ReceiptDTO> parsedList = parserJsonStringUtil.mergeRawMultipleJsonArrays(jsonText);
//        return foodVectorService.findSimilarCategories(parsedList);
//    }
}

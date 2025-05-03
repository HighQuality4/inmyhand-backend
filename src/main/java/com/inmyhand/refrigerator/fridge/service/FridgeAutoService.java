package com.inmyhand.refrigerator.fridge.service;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inmyhand.refrigerator.category.service.FoodVectorService;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.fridge.service.ocr.ReceiptExtraction;
import com.inmyhand.refrigerator.util.ParserJsonStringUtil;

@Service
@RequiredArgsConstructor
public class FridgeAutoService{
    // 냉장고 ocr 카테고리 자동입력 로직
	
	

    private final  ReceiptExtraction extraction;
    private final  FoodVectorService foodVectorService;
    private final ParserJsonStringUtil parserJsonStringUtil;

    
    
    // ocr + 카테고리 매칭 로직
    public List<ReceiptDTO> svcOcrTotalTest(List<MultipartFile> files) {
    	String result = extraction.extractReceiptData(files);
    	List<ReceiptDTO> parsedList = parserJsonStringUtil.mergeRawMultipleJsonArrays(result);
		List<ReceiptDTO> realTextList = foodVectorService.findSimilarCategories(parsedList);
        return realTextList;
    }




//    
//    public List<ReceiptDTO> search(@RequestBody String jsonText) {
//        List<ReceiptDTO> parsedList = parserJsonStringUtil.mergeRawMultipleJsonArrays(jsonText);
//        return foodVectorService.findSimilarCategories(parsedList);
//    }
}

package com.inmyhand.refrigerator.category.service;

import com.inmyhand.refrigerator.category.EmbeddingUtil;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.repository.FoodVectorRepository;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodVectorService {

    private final FoodVectorRepository foodVectorRepository;
    private final AsyncService asyncService;
    private final EmbeddingUtil embeddingUtil;

    public void saveFallbackFoodVector(String categoryName, String naturalText, int expirationInfo, String inputText) {
        Float[] embedding = embeddingUtil.getEmbedding(List.of(naturalText)).get(0);

        String embeddingStr = "[" + Arrays.stream(embedding)
                .map(f -> String.format(Locale.US, "%.8f", f))  // float to string 강제 변환
                .collect(Collectors.joining(",")) + "]";

        // 카테고리 일관성을 위한 insertFoodVector input값 조정(embedding 값만 다르게)
        foodVectorRepository.insertFoodVector(categoryName, naturalText, expirationInfo, embeddingStr);
    }

    public List<ReceiptDTO> findSimilarCategories(List<ReceiptDTO> receiptList) {

        List<CompletableFuture<ReceiptDTO>> futures = receiptList.stream()
                .map(asyncService::processInput)
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

}

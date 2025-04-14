package com.inmyhand.refrigerator.category.service;

import com.inmyhand.refrigerator.category.EmbeddingUtil;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.domain.dto.GptResponseDTO;
import com.inmyhand.refrigerator.category.GptUtil;
import com.inmyhand.refrigerator.category.repository.FoodVectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodVectorService {

    private final FoodVectorRepository foodVectorRepository;
    private final EmbeddingUtil embeddingUtil;
    private final GptUtil gptUtil;

    public void saveFallbackFoodVector(String categoryName, String naturalText, int expirationInfo, String inputText) {
        Float[] embedding = embeddingUtil.getEmbedding(List.of(inputText)).get(0);

        String embeddingStr = "[" + Arrays.stream(embedding)
                .map(f -> String.format(Locale.US, "%.8f", f))  // float to string 강제 변환
                .collect(Collectors.joining(",")) + "]";

        // 카테고리 일관성을 위한 insertFoodVector input값 조정(embedding 값만 다르게)
        foodVectorRepository.insertFoodVector(categoryName, categoryName, expirationInfo, embeddingStr);
    }

    public List<FoodVectorRequestDTO> findSimilarCategories(List<String> inputTextList) {

        List<FoodVectorRequestDTO> resultList = new ArrayList<>();

        for (String inputText : inputTextList) {

            // 1. 기존 유사 카테고리 검색 (임베딩 → 검색)
            Float[] embedding = embeddingUtil.getEmbedding(List.of(inputText)).get(0);

            String vector = "'[" + Arrays.stream(embedding)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "]'";

//            FoodVectorRequestDTO result = foodVectorRepository.findMostSimilarCategoryByVector(inputText, vector);
            FoodVectorRequestDTO result = foodVectorRepository.findMostSimilarCategoryByVector(inputText, vector).orElseThrow(() -> new RuntimeException("Could not find most similar category"));

            // 2. distance 기준 판단 (0.5 넘으면 폴백)
            if (result.getDistance() > 0.3) {
                // GPT 호출
                GptResponseDTO gptData = gptUtil.getCategoryAndExpiration(inputText);

                // DB Insert (category_name은 식품명 그대로)
                saveFallbackFoodVector(gptData.getCategory(), inputText, gptData.getExpirationInfo(), inputText);

                // 폴백 데이터로 직접 응답 구성
                result = FoodVectorRequestDTO.builder()
                        .inputText(inputText)
                        .categoryName(inputText)
                        .naturalText(gptData.getCategory())
                        .distance(0.0)  // 새로 넣었으니까
                        .build();
            }

            resultList.add(result);
        }

        return resultList;
    }


}


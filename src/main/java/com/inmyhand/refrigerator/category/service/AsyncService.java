package com.inmyhand.refrigerator.category.service;

import com.inmyhand.refrigerator.category.ClaudeUtil;
import com.inmyhand.refrigerator.category.EmbeddingUtil;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.domain.dto.PromptResponseDTO;
import com.inmyhand.refrigerator.category.repository.FoodVectorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncService {

    private final ClaudeUtil claudeUtil;
    private final EmbeddingUtil embeddingUtil;
    private final FoodVectorRepository foodVectorRepository;
    private final AsyncInsertService asyncInsertService;

    @Async
    public CompletableFuture<FoodVectorRequestDTO> fallbackForInput(String inputText, String dummy) {
        try {
            // 1. 카테고리 + 유통기한 + 설명문 한 번에 추출
            PromptResponseDTO catData = claudeUtil.getCombinedCategoryAndExplanation(inputText);

            // 2. 임베딩 생성 (catData.naturalText 기준)
            Float[] embedding = embeddingUtil.getEmbedding(List.of(catData.getNaturalText())).get(0);

            // 3. 벡터 → 문자열 변환
            String embeddingStr = "[" + Arrays.stream(embedding)
                    .map(f -> String.format(Locale.US, "%.8f", f))
                    .collect(Collectors.joining(",")) + "]";

            // 4. DB 저장
            asyncInsertService.insertVector(
                    catData.getCategory(),
                    catData.getNaturalText(),
                    catData.getExpirationInfo(),
                    embedding
            );

            // 5. 결과 DTO 구성
            FoodVectorRequestDTO result = FoodVectorRequestDTO.builder()
                    .inputText(inputText)
                    .categoryName(catData.getCategory())
                    .expirationInfo(catData.getExpirationInfo())
                    .naturalText(catData.getNaturalText())
                    .distance(0.0)
                    .build();

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            log.warn("폴백 처리 실패: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<FoodVectorRequestDTO> processInput(String inputText) {
        try {
            // 1. 설명 생성 → 제거 (이제 Claude 한 번만 호출)
            // 2. Claude 통합 호출 결과
            PromptResponseDTO catData = claudeUtil.getCombinedCategoryAndExplanation(inputText);

            // 3. 임베딩
            Float[] embedding = embeddingUtil.getEmbedding(List.of(catData.getNaturalText())).get(0);
            String vector = "'[" + Arrays.stream(embedding)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "]'";

            // 4. 유사도 검색
            Optional<FoodVectorRequestDTO> optionalResult =
                    foodVectorRepository.findMostSimilarCategoryByVector(catData.getNaturalText(), vector);

            // 5. fallback 판단
            if (optionalResult.isEmpty() || optionalResult.get().getDistance() > 0.28) {
                return fallbackForInput(inputText, null); // dummy 자리
            }

            return CompletableFuture.completedFuture(optionalResult.get());

        } catch (Exception e) {
            log.warn("processInput 실패: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }


}

package com.inmyhand.refrigerator.category.service;

import com.inmyhand.refrigerator.category.ClaudeUtil;
import com.inmyhand.refrigerator.category.EmbeddingUtil;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.domain.dto.PromptResponseDTO;
import com.inmyhand.refrigerator.category.repository.FoodVectorRepository;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public CompletableFuture<ReceiptDTO> fallbackForInput(ReceiptDTO receiptDTO, PromptResponseDTO catData) {
        try {
            // 1. 카테고리 + 유통기한 + 설명문 한 번에 추출
            // PromptResponseDTO catData = claudeUtil.getCombinedCategoryAndExplanation(inputText);

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

            // 5. DTO에 결과 반영
            receiptDTO.setCategory(catData.getCategory());

            if (receiptDTO.getPurchaseDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 1. Date → LocalDate (구매일을 서울 자정 기준으로 변환)
                LocalDate purchase = receiptDTO.getPurchaseDate().toInstant()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toLocalDate();

                // 2. 유통기한 일수 더하기
                LocalDate expire = purchase.plusDays(catData.getExpirationInfo());

                // 3. LocalDate → Date (서울 자정 기준으로 Date 변환)
                Date expireDate = Date.from(expire.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant());

                // 4. 저장
                receiptDTO.setExpirationDate(expireDate);
            }

            return CompletableFuture.completedFuture(receiptDTO);

        } catch (Exception e) {
            log.warn("폴백 처리 실패: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<ReceiptDTO> processInput(ReceiptDTO receiptDTO) {
        try {
            String inputText = receiptDTO.getProduct();

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
                return fallbackForInput(receiptDTO, catData); // dummy 자리
            }

            // 6. Claude 결과를 dto에 반영
            receiptDTO.setCategory(catData.getCategory());

            if (receiptDTO.getPurchaseDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 1. Date → LocalDate (구매일을 서울 자정 기준으로 변환)
                LocalDate purchase = receiptDTO.getPurchaseDate().toInstant()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toLocalDate();

                // 2. 유통기한 일수 더하기
                LocalDate expire = purchase.plusDays(catData.getExpirationInfo());

                // 3. LocalDate → Date (서울 자정 기준으로 Date 변환)
                Date expireDate = Date.from(expire.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant());

                // 4. 저장
                receiptDTO.setExpirationDate(expireDate);
            }

            return CompletableFuture.completedFuture(receiptDTO);

        } catch (Exception e) {
            log.warn("processInput 실패: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }


}

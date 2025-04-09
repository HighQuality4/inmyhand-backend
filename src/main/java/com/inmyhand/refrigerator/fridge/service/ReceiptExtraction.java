package com.inmyhand.refrigerator.fridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 영수증
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ReceiptExtraction {


    private final VertexAiGeminiChatModel chatModel;
    private final ObjectMapper objectMapper;


    /**
     * @param files
     * @return
     * @throws IOException
     */
    public List<ReceiptDTO> extractReceiptData(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("전송된 영수증 이미지 파일 목록이 비어있습니다");
        }
        List<ReceiptDTO> receiptList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                // 빈 파일은 건너뜁니다.
                log.error("비어있는 이미지 파일은 스킵합니다.");
                continue;
            }

            // AI 요청 메시지 생성
            UserMessage userMessage = new UserMessage(
                    "이 영수증 이미지에서 product, quantity, purchaseDate 정보를 추출해주세요. " +
                            "먹을 수 있는 식재료만 추출해 주세요. "+
                            "중복은 제거하고, 영수증이 아닐 경우 error를 반환해 주세요. " +
                            "결과는 반드시 아래와 같이 JSON 배열 형식으로 정확히 반환해야 합니다: " +
                            "{\"product\": \"상품명\", \"quantity\": 수량, \"purchaseDate\": \"구매일자(YYYY-MM-DD)\"}",
                    List.of(new Media(
                            MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())),
                            new InputStreamResource(file.getInputStream())
                    ))
            );

            // AI 모델 호출
            ChatResponse response = chatModel.call(new Prompt(List.of(userMessage)));
            String jsonResponse = response.getResult().getOutput().getText();

            // AI 응답에서 실제 JSON 부분만 추출
            String cleanedJson = extractJsonFromResponse(jsonResponse);
            System.out.println("json=" + cleanedJson);

            try {
                // JSON 문자열을 List<ReceiptDTO>로 파싱
                List<ReceiptDTO> fileReceipts = objectMapper.readValue(
                        cleanedJson, new TypeReference<List<ReceiptDTO>>() {}
                );
                // 데이터가 존재할 경우에만 리스트에 추가 (중복 제거 등 추가 로직 가능)
                if (fileReceipts != null && !fileReceipts.isEmpty()) {
                    receiptList.addAll(fileReceipts);
                }
            } catch (JsonProcessingException e) {
                // 만약 특정 파일에 대해 파싱 오류가 발생하면 로그 출력 후 계속 진행
                System.err.println("영수증 데이터 파싱 오류(파일 건너뜀): " + e.getMessage());
            }
        }
        return receiptList;
    }


    /**
     * AI 응답에서 JSON 부분만 추출하는 도우미 메소드
     */
    public String extractJsonFromResponse(String response) {

        // 응답 문자열 좌우 공백 제거
        String trimmedResponse = Objects.requireNonNull(response).trim();

        // 응답 전체가 JSON 배열일 경우: [ ... ]
        if (trimmedResponse.startsWith("[") && trimmedResponse.endsWith("]")) {
            log.warn("Extracted JSON Array = {}", trimmedResponse);
            return trimmedResponse;
        }

        // 응답 전체가 JSON 객체일 경우: { ... }
        if (trimmedResponse.startsWith("{") && trimmedResponse.endsWith("}")) {
            log.warn("Extracted JSON Object = {}", trimmedResponse);
            return trimmedResponse;
        }

        // 만약 JSON 데이터가 다른 문자열 사이에 포함되어 있을 경우,
        // 첫 번째 '[' 또는 '{'부터 마지막 ']' 또는 '}'까지 추출한다.
        int indexBracket = trimmedResponse.indexOf('[');
        int indexBrace   = trimmedResponse.indexOf('{');
        // 둘 중 더 먼저 나오는 문자 위치를 startIdx로 사용
        int startIdx = -1;
        if (indexBracket == -1 && indexBrace == -1) {
            throw new RuntimeException("응답에서 유효한 JSON을 찾을 수 없습니다: " + response);
        } else if (indexBracket == -1) {
            startIdx = indexBrace;
        } else if (indexBrace == -1) {
            startIdx = indexBracket;
        } else {
            startIdx = Math.min(indexBracket, indexBrace);
        }

        // 마지막 ']'와 '}' 중 더 늦게 나오는 위치를 endIdx로 사용
        int endIdxBracket = trimmedResponse.lastIndexOf(']');
        int endIdxBrace   = trimmedResponse.lastIndexOf('}');
        int endIdx = Math.max(endIdxBracket, endIdxBrace);

        if (startIdx >= 0 && endIdx != -1 && endIdx > startIdx) {
            String extractedJson = trimmedResponse.substring(startIdx, endIdx + 1);
            log.warn("Extracted JSON = {}", extractedJson);
            return extractedJson;
        }

        throw new RuntimeException("응답에서 유효한 JSON을 찾을 수 없습니다: " + response);
    }
}

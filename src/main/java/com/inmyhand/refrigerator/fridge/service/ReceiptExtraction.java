package com.inmyhand.refrigerator.fridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.error.exception.NotFoundJsonException;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gemini.receipt.prompt}")
    private String prompt;


    /**
     * @param files
     * @return
     * @throws IOException
     */
    public List<ReceiptDTO> extractReceiptData(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return List.of();
        }

        List<ReceiptDTO> receiptList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                // 빈 파일은 건너뜁니다.
                log.error("비어있는 이미지 파일은 스킵합니다.");
                continue;
            }
            UserMessage userMessage = null;
            // AI 요청 메시지 생성
            try {
                userMessage = new UserMessage(
                        prompt,
                        List.of(new Media(
                                MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())),
                                new InputStreamResource(file.getInputStream())
                        ))
                );
            } catch (IOException e) {
                log.error("AI 요청 메시지 오류");
                log.error(e.getMessage());
            }
            // userMessage 이 null 일 시 아래 로직은 건너뜀
            if(userMessage == null) {
                continue;
            }

            // AI 모델 호출
            ChatResponse response = chatModel.call(new Prompt(List.of(userMessage)));
            String jsonResponse = response.getResult().getOutput().getText();

            // AI 응답에서 실제 JSON 부분만 추출
            String cleanedJson = extractJsonFromResponse(jsonResponse);

            List<ReceiptDTO> receiptDTOS = jsonToReceipt(cleanedJson);

            if (receiptDTOS != null && !receiptDTOS.isEmpty()) {
                receiptList.addAll(receiptDTOS);
            }
        }

        return receiptList;
    }

    /**
     *
     * @param json
     * @return
     *
     * String 로 되어 있는 json을 List<ReceiptDTO>로 파싱
     */
    public List<ReceiptDTO> jsonToReceipt(String json) {
        try {
            // JSON 문자열을 List<ReceiptDTO>로 파싱
            return objectMapper.readValue(
                    json, new TypeReference<List<ReceiptDTO>>() {
                    }
            );
        } catch (JsonProcessingException e) {
            // 만약 특정 파일에 대해 파싱 오류가 발생하면 로그 출력 후 계속 진행
            log.error("영수증 데이터 파싱 오류(파일 건너뜀): {}", e.getMessage());
            return null;
        }
    }


    /**
     *
     * @param response
     * @return
     * AI 응답에서 JSON 부분만 추출하는 도우미 메소드
     */
    public String extractJsonFromResponse(String response) {

        // 응답 문자열 좌우 공백 제거
        String trimmedResponse = Objects.requireNonNull(response).trim();

        // 응답 전체가 JSON 배열일 경우: [ ... ]
        if (trimmedResponse.startsWith("[") && trimmedResponse.endsWith("]")) {
//            log.warn("Extracted JSON Array = {}", trimmedResponse);
            return trimmedResponse;
        }

        // 응답 전체가 JSON 객체일 경우: { ... }
        if (trimmedResponse.startsWith("{") && trimmedResponse.endsWith("}")) {
//            log.warn("Extracted JSON Object = {}", trimmedResponse);
            return trimmedResponse;
        }

        // 만약 JSON 데이터가 다른 문자열 사이에 포함되어 있을 경우,
        // 첫 번째 '[' 또는 '{'부터 마지막 ']' 또는 '}'까지 추출한다.
        int indexBracket = trimmedResponse.indexOf('[');
        int indexBrace = trimmedResponse.indexOf('{');
        // 둘 중 더 먼저 나오는 문자 위치를 startIdx로 사용
        int startIdx = -1;
        if (indexBracket == -1 && indexBrace == -1) {
            throw new NotFoundJsonException("응답에서 유효한 JSON을 찾을 수 없습니다: " + response);
        } else if (indexBracket == -1) {
            startIdx = indexBrace;
        } else if (indexBrace == -1) {
            startIdx = indexBracket;
        } else {
            startIdx = Math.min(indexBracket, indexBrace);
        }

        // 마지막 ']'와 '}' 중 더 늦게 나오는 위치를 endIdx로 사용
        int endIdxBracket = trimmedResponse.lastIndexOf(']');
        int endIdxBrace = trimmedResponse.lastIndexOf('}');
        int endIdx = Math.max(endIdxBracket, endIdxBrace);

        if (startIdx >= 0 && endIdx != -1 && endIdx > startIdx) {
            String extractedJson = trimmedResponse.substring(startIdx, endIdx + 1);
//            log.warn("Extracted JSON = {}", extractedJson);
            return extractedJson;
        }

        throw new NotFoundJsonException("응답에서 유효한 JSON을 찾을 수 없습니다: " + response);
    }
}

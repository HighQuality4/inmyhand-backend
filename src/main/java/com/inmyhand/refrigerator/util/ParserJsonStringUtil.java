package com.inmyhand.refrigerator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Component
@Slf4j
public class ParserJsonStringUtil {
    public List<String> parseRawProductText(String rawText, String parseStandard) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(rawText);
            if (!root.isArray()) return List.of();

            return StreamSupport.stream(root.spliterator(), false)
                    .map(node -> node.get(parseStandard).asText())
                    .filter(s -> !s.isBlank())
                    .distinct()
                    .toList();

        } catch (Exception e) {
            log.warn("JSON 파싱 오류: {}", e.getMessage());
            return List.of();
        }
    }

    public List<ReceiptDTO> mergeRawMultipleJsonArrays(String rawText) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // 날짜 포맷 설정

        List<ReceiptDTO> resultList = new ArrayList<>();

        // 1. 배열 경계 붙이기: ][ → ],[
        String fixed = rawText.replaceAll("](\\s*)\\[", "],[");

        // 2. 패턴으로 배열 덩어리 추출
        Pattern pattern = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fixed);

        while (matcher.find()) {
            String arrayChunk = "[" + matcher.group(1).trim() + "]";
            try {
                List<ReceiptDTO> chunk = mapper.readValue(arrayChunk, new TypeReference<>() {});
                resultList.addAll(chunk);
            } catch (Exception e) {
                log.warn("부분 JSON 파싱 실패: {}", e.getMessage());
            }
        }

        return resultList;
    }
}

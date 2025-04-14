package com.inmyhand.refrigerator.category;

import com.inmyhand.refrigerator.category.domain.dto.GptResponseDTO;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GptUtil {

    @Value("${spring.ai.openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public GptResponseDTO getCategoryAndExpiration(String inputText) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", buildPrompt(inputText));

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4-turbo");
        requestBody.put("messages", messages);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        String response = restTemplate.postForObject(API_URL, entity, String.class);
        JSONObject json = new JSONObject(response);
        String content = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        String replaceContent = content.replaceAll("```|json", "");
        JSONObject contentJson = new JSONObject(replaceContent);
        System.out.println(replaceContent);

        return GptResponseDTO.builder()
                .category(contentJson.getString("category"))
                .expirationInfo(contentJson.getInt("expiration_info"))
                .build();
    }

    private String buildPrompt(String inputText) {
        return """
        아래 식품명이 고기면 고기 부위만, 생선이면 생선 이름, 과일이나 야채도 이름을, 그 외에는 식품명을 그대로 최소 카테고리(category)로 지정하고 웹 검색을 통해서 평균 유통기한(expiration_info)을 일수로 숫자로만 알려줘. JSON 형태로만 답해.

        식품명: "%s"
        """.formatted(inputText);
    }
}

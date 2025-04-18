package com.inmyhand.refrigerator.category;

import com.inmyhand.refrigerator.category.domain.dto.PromptResponseDTO;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClaudeUtil {

    @Value("${spring.ai.anthropic.api-key}")
    private String apiKey;

    @Value("${spring.ai.anthropic.base-url}")
    private String API_URL;

    //카테고리 및 유통기한 검색 메서드

    public PromptResponseDTO getCombinedCategoryAndExplanation(String inputText) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.get("application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "claude-3-haiku-20240307");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", "다음 JSON 형식으로만 응답하세요. 키는 category, expiration_info, naturalText만 사용하세요.");

        JSONArray messages = new JSONArray();

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", buildCombinedPrompt(inputText));
        messages.put(userMessage);

        requestBody.put("messages", messages);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), mediaType))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Claude API 호출 실패: " + response.code() + " - " + response.body().string());
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            String content = json.getJSONArray("content")
                    .getJSONObject(0)
                    .getString("text");

            System.out.println("Claude 응답: " + content);

            String cleaned = content.replaceAll("```|json", "").trim();
            JSONObject result = new JSONObject(cleaned);

            return PromptResponseDTO.builder()
                    .category(result.getString("category"))
                    .expirationInfo(result.getInt("expiration_info"))
                    .naturalText(result.getString("naturalText"))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Claude 통신 오류", e);
        }
    }

    private String buildCombinedPrompt(String inputText) {
        return """
    아래 식품명을 바탕으로 다음 JSON 항목을 구성하세요:

    - category: 고기면 부위명, 생선이면 이름, 과일/야채는 이름. 불명확하면 식품명 또는 음식 종류를 그대로 사용(여기에 설명을 달지 말것! 그리고 반드시 한국어로 적을 것! 그리고 일관되게 적을 것)
    - expiration_info: 평균 유통기한 (일 수, 없으면 1), 소주나 보드카처럼 도수가 높은 술이면 365일로 할 것(맥주는 120일, 막걸리는 7일)
    - naturalText: 주재료, 조리방식, 섭취상황, 맛을 포함한 100자 이내 설명 (예: "된장, 감자, 두부 등을 끓여 먹는 구수한 아침용 한국 찌개입니다."), 음식명만 설명하는 것은 안됨!

    반드시 다음 JSON 형식으로만 응답하세요:

    {
      "category": "...",
      "expiration_info": ...,
      "naturalText": "..."
    }

    식품명: "%s"
    """.formatted(inputText);
    }

}
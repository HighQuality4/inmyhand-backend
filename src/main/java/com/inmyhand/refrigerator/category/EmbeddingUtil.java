package com.inmyhand.refrigerator.category;


import com.fasterxml.jackson.databind.json.JsonMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//Cohere 사용함 : 월 5만 토큰 무료~
@Component
public class EmbeddingUtil {

    @Value("${spring.ai.cohere.api-key}")
    private String apiKey;

    @Value("${spring.ai.cohere.embed-url}")
    private String API_URL;

    public List<Float[]> getEmbedding(List<String> textList) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);  // Bearer {API_KEY}

        JsonMapper jsonMapper = new JsonMapper();
        // Cohere Embed API 전용 RequestBody
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "embed-multilingual-v3.0"); // 한국어 포함 모델
        requestBody.put("texts", textList);                  // Cohere는 texts key 사용
        requestBody.put("input_type", "search_document");   // 추천값: search_document

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        JSONArray embeddingsArray = json.getJSONArray("embeddings");  // 핵심 응답 필드

        List<Float[]> embeddingList = new ArrayList<>();

        // 임베딩한 벡터값 Float[] 형식으로 변환
        for (int i = 0; i < embeddingsArray.length(); i++) {
            JSONArray vector = embeddingsArray.getJSONArray(i);
            Float[] embedding = new Float[vector.length()];

            for (int j = 0; j < vector.length(); j++) {
                embedding[j] = vector.getFloat(j);
            }
            embeddingList.add(embedding);
        }

        return embeddingList;
    }
}


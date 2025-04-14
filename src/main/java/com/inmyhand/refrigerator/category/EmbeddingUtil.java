package com.inmyhand.refrigerator.category;


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

@Component
public class EmbeddingUtil {

    @Value("${spring.ai.openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/embeddings";

    public List<Float[]> getEmbedding(List<String> text) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "text-embedding-3-small");
        requestBody.put("input", text);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        JSONArray dataArray = json.getJSONArray("data");
        List<Float[]> list = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONArray embeddingArray = dataArray.getJSONObject(i).getJSONArray("embedding");
            Float[] embedding = new Float[embeddingArray.length()];
            for (int j = 0; j < embeddingArray.length(); j++) {
                embedding[j] = embeddingArray.getFloat(j);
            }
            list.add(embedding);
        }

        return list;
    }
}


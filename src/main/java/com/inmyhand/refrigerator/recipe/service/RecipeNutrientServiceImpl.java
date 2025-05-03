package com.inmyhand.refrigerator.recipe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.error.exception.NotFoundJsonException;
import com.inmyhand.refrigerator.healthinfo.service.HealthInfoService;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.domain.dto.RecipeNutrientAnalysisEntityDto;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// 영양소 분석 전용 서비스
@Service
@RequiredArgsConstructor
public class RecipeNutrientServiceImpl implements RecipeNutrientService {

    @Autowired
    private final RecipeInfoRepository recipeInfoRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final HealthInfoService healthInfoService;

    private final VertexAiGeminiChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Value("${gemini.nutrition.promptTemplate}")
    private String promptTemplate;

    public RecipeNutrientAnalysisEntityDto getNutritionAnalysis(Long recipeId, Long memberId) {
        // 1. 레시피, 멤버 조회
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. ID: " + memberId));

        RecipeInfoEntity recipe = recipeInfoRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다. ID: " + recipeId));

        // 2. 프롬프트 구성
        String ingredients = recipe.getRecipeIngredientList().stream()
                .map(i -> i.getIngredientName() + " " + i.getIngredientQuantity()+i.getIngredientUnit()) // ex: 가지 1개
                .collect(Collectors.joining(", "));

        String interests = healthInfoService.getHealthInterest(memberId).stream()
                .collect(Collectors.joining(", "));

        String finalPrompt = promptTemplate
                .replace("{{ingredients}}", ingredients)
                .replace("{{recipeName}}", recipe.getRecipeName())
                .replace("{{interest}}", interests);

        // 3. Gemini 호출
        UserMessage userMessage = new UserMessage(finalPrompt);
        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage)));
        String rawOutput = response.getResult().getOutput().getText();

        // 4. JSON 파싱
        String json = extractJson(rawOutput);
        System.out.println(json);

        try {
            return objectMapper.readValue(json, RecipeNutrientAnalysisEntityDto.class);
        } catch (Exception e) {
            throw new RuntimeException("영양 분석 결과 파싱 실패");
        }
    }

    private String extractJson(String response) {
        String trimmed = Objects.requireNonNull(response).trim();

        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return trimmed;
        }

        int start = Math.min(
                indexOrMax(trimmed.indexOf('{')),
                indexOrMax(trimmed.indexOf('['))
        );
        int end = Math.max(
                trimmed.lastIndexOf('}'),
                trimmed.lastIndexOf(']')
        );

        if (start >= 0 && end > start) {
            return trimmed.substring(start, end + 1);
        }

        throw new NotFoundJsonException("Gemini 응답에서 유효한 JSON을 찾을 수 없습니다:\n" + response);
    }

    private int indexOrMax(int index) {
        return index == -1 ? Integer.MAX_VALUE : index;
    }
}

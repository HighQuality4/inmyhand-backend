package com.inmyhand.refrigerator.healthinfo.controller;


import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import com.inmyhand.refrigerator.healthinfo.service.HealthInfoServiceImpl;
import com.inmyhand.refrigerator.member.domain.dto.LoginRequestDTO;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/healthInfo")
public class HealthInfoRestController {

    private final HealthInfoServiceImpl healthInfoService;

    @PostMapping("/allergy")
    public ResponseEntity<List<HealthInfoDTO>> ctlGetAllergyInfo(@Param("memberId") Long memberId) {

        List<HealthInfoDTO> healthInfoList = healthInfoService.getAllergyInfo(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/hate_food")
    public ResponseEntity<List<HealthInfoDTO>> ctlGetHateFoodInfo(@Param("memberId") Long memberId) {
        List<HealthInfoDTO> healthInfoList = healthInfoService.getHateFoodInfo(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/health_interest")
    public ResponseEntity<List<HealthInfoDTO>> ctlGetHealthInterestInfo(@Param("memberId") Long memberId) {
        List<HealthInfoDTO> healthInfoList = healthInfoService.getHealthInterest(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/api/search/health_interest")
    public ResponseEntity<Map<String, Object>> searchHealthInfo(DataRequest dataRequest) {

        System.out.println(dataRequest);
        List<String> HEALTH_CATEGORIES = healthInfoService.getAllInterestInfoCategory();

        System.out.println(HEALTH_CATEGORIES.toString());

        String keyword = dataRequest.getParameter("filter");
        System.out.println(keyword);

        List<List<String>> rows = HEALTH_CATEGORIES.stream()
                .filter(category -> category.contains(keyword))
                .limit(5)
                .map(item -> List.of(item, item))  // ["label값", "value값"]
                .collect(Collectors.toList());

        // columns 고정
        List<Map<String, String>> columns = List.of(
                Map.of("name", "label"),
                Map.of("name", "value")
        );

        // 최종 결과 만들기
        Map<String, Object> result = new HashMap<>();
        result.put("columns", columns);
        result.put("rows", rows);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/search/ingredients")
    public List<Map<String, String>> searchIngredients(@RequestParam("keyword") String keyword) {

        List<String> INGREDIENT_CATEGORIES = healthInfoService.getAllRecipeIngredientCategory();

        return INGREDIENT_CATEGORIES.stream().filter(category -> category.contains(keyword))
                .limit(5)
                .map(item -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("label", item);
                    map.put("value", item);
                    System.out.println(map);
                    return map;
                })
                .collect(Collectors.toList());
    }

}
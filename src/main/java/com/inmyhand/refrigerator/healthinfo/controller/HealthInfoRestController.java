package com.inmyhand.refrigerator.healthinfo.controller;


import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.spring.JSONDataView;
import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import com.inmyhand.refrigerator.healthinfo.service.HealthInfoServiceImpl;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public View searchHealthInfo(DataRequest dataRequest) throws IOException {

        List<String> HEALTH_CATEGORIES = healthInfoService.getAllInterestInfoCategory();
        List<Map<String, Object>> categoryList = new ArrayList<>();
        HEALTH_CATEGORIES.stream()
                .limit(5)
                .forEach(item -> {
                    Map<String, Object> rowData = new HashMap<>();
                    rowData.put("label", item);
                    rowData.put("value", item);
                    categoryList.add(rowData);
                });

        Map<String, Object> meta = new HashMap<String, Object>();
        dataRequest.setMetadata(true, meta);
        dataRequest.setResponse("dsQuickSearch", categoryList);

        return new JSONDataView();
    }

    @PostMapping("/api/search/ingredients")
    public View searchIngredients(DataRequest dataRequest) {

        List<String> INGREDIENT_CATEGORIES = healthInfoService.getAllRecipeIngredientCategory();
        List<Map<String, Object>> categoryList = new ArrayList<>();
        INGREDIENT_CATEGORIES.stream()
                .limit(5)
                .forEach(item -> {
                    Map<String, Object> rowData = new HashMap<>();
                    rowData.put("label", item);
                    rowData.put("value", item);
                    categoryList.add(rowData);
                });

        Map<String, Object> meta = new HashMap<String, Object>();
        dataRequest.setMetadata(true, meta);
        dataRequest.setResponse("dsQuickSearch", categoryList);

        return new JSONDataView();
    }

    @PostMapping("/api/save")
    public ResponseEntity<?> ctlSaveHealthInfo(@AuthenticationPrincipal CustomUserDetails custom, DataRequest dataRequest) {
        HealthInfoDTO health = ConverterClassUtil.getSingleClass(dataRequest, "dmHealthInfo", HealthInfoDTO.class);
        System.out.println(dataRequest.toString());
        System.out.println(health.getAllergy());
        System.out.println(health.getHateFood());
        System.out.println(health.getInterestInfo());

        System.out.println(custom.getUserId());
        healthInfoService.saveHealthInfo(custom.getUserId(), health);

        return ResponseEntity.ok("dd");
    }

}
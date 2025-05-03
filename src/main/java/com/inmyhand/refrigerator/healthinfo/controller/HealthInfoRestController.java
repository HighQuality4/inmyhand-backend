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
@RequestMapping("/api/healthInfo")
public class HealthInfoRestController {

    private final HealthInfoServiceImpl healthInfoService;

    @PostMapping("/allergy")
    public ResponseEntity<List<String>> ctlGetAllergyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long memberId = customUserDetails.getUserId();
        List<String> healthInfoList = healthInfoService.getAllergyInfo(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/hate_food")
    public ResponseEntity<List<String>> ctlGetHateFoodInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long memberId = customUserDetails.getUserId();
        List<String> healthInfoList = healthInfoService.getHateFoodInfo(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/health_interest")
    public ResponseEntity<List<String>> ctlGetHealthInterestInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long memberId = customUserDetails.getUserId();
        List<String> healthInfoList = healthInfoService.getHealthInterest(memberId);
        return new ResponseEntity<>(healthInfoList, HttpStatus.OK);
    }

    @PostMapping("/search/health_interest")
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

    @PostMapping("/search/ingredients")
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

    @PostMapping("/save")
    public ResponseEntity<?> ctlSaveHealthInfo(@AuthenticationPrincipal CustomUserDetails custom, DataRequest dataRequest) {
        HealthInfoDTO health = ConverterClassUtil.getSingleClass(dataRequest, "dmHealthInfo", HealthInfoDTO.class);
        healthInfoService.saveHealthInfo(custom.getUserId(), health);

        return ResponseEntity.ok("dd");
    }

}
package com.inmyhand.refrigerator.healthinfo.controller;


import com.inmyhand.refrigerator.healthinfo.domain.dto.HealthInfoDTO;
import com.inmyhand.refrigerator.healthinfo.service.HealthInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
package com.inmyhand.refrigerator.fridge.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeFoodService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupRoleService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.fridge.domain.dto.food.OcrFoodDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeAutoService;
import com.inmyhand.refrigerator.util.ConverterClassUtil;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@Slf4j
public class FoodOcrController {

    private final FridgeAutoService fridgeAutoService;
    private final FridgeFoodService fridgeFoodService;
    private final FridgeGroupRoleService fridgeGroupRoleService;

    @PostMapping("/create")
    public ResponseEntity<?> getChangeFridgeFoods(DataRequest dataRequest,@AuthenticationPrincipal CustomUserDetails userDetails) {
    	log.info("요청에 포함된 : {}", dataRequest.getResponse());
        List<OcrFoodDTO> classList = ConverterClassUtil.getClassList(dataRequest, "ocrAllList", OcrFoodDTO.class,"yyyyMMdd");

        //---------------------------------------------------------------------

        Long userId = userDetails.getUserId();
//        Long memberId = 3L;

        //---------------------------------------------------------------------

        // 유저가 소유한 leader 냉장고에 저장하기
        Long fridgeId = fridgeGroupRoleService.getMyLeaderFridgeId(userId);
        System.out.println("냉장고 id 확인하기 >>>"+fridgeId);

        List<FridgeFoodDTO> fridgeFoodList = new ArrayList<>();
        for (OcrFoodDTO dto : classList) {
            FridgeFoodDTO food = FridgeFoodDTO.builder()
                    .foodName(dto.getFoodName())
                    .foodAmount(dto.getFoodAmount())
                    .chargeDate(dto.getChargeDate())
                    .endDate(dto.getEndDate())
                    .saveDate(new Date()) // 저장일은 현재 시간
                    .categoryName(dto.getCategoryName())
                    .fridgeId(fridgeId)
                    .build();

            fridgeFoodList.add(food);
        }
        // 냉장고에 저장하기
        fridgeFoodService.svcCreateFridgeFood(fridgeId, fridgeFoodList);
        
        return ResponseEntity.ok("ok");
    }

    
}

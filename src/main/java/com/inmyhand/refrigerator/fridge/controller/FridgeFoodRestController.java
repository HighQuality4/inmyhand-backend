package com.inmyhand.refrigerator.fridge.controller;

import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeMainPageDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fridge")
@RequiredArgsConstructor
public class FridgeFoodRestController {

    private final FridgeFoodService fridgeFoodService;

    // 메인 냉장고의 전체 식재료 리스트 조회
    @GetMapping("/main/{memberId}/foods")
    public ResponseEntity<FridgeMainPageDTO> getMainFridgeFoods(@PathVariable Long memberId) {
        FridgeMainPageDTO foodsInfo = fridgeFoodService.svcGetFridgeMainPage(memberId);
        return ResponseEntity.ok(foodsInfo);
    }




    @PostMapping
    public ResponseEntity<Void> createFridgeFood(
            @PathVariable Long fridgeId,
            @RequestBody FridgeFoodDTO dto) {

        dto.setFridgeId(fridgeId); // fridgeId 설정
        fridgeFoodService.svcCreateFridgeFood(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 식재료 수정
    @PutMapping("/{foodId}")
    public ResponseEntity<Void> updateFridgeFood(
            @PathVariable Long fridgeId,
            @PathVariable Long foodId,
            @RequestBody FridgeFoodDTO dto) {

        dto.setFridgeId(fridgeId); // fridgeId 유지
        fridgeFoodService.svcUpdateFridgeFood(foodId, dto);
        return ResponseEntity.ok().build();
    }

    // 3. 식재료 삭제 (DELETE /api/fridges/{fridgeId}/foods/{foodId})
    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> deleteFridgeFood(
            @PathVariable Long fridgeId,
            @PathVariable Long foodId) {

        fridgeFoodService.svcDeleteFridgeFood(foodId);
        return ResponseEntity.noContent().build();
    }

    // 4. 냉장고별 전체 식재료 조회 (GET /api/fridges/{fridgeId}/foods)
    @GetMapping
    public ResponseEntity<List<FridgeFoodDTO>> getAllFridgeFoods(@PathVariable Long fridgeId) {
        return ResponseEntity.ok(fridgeFoodService.svcGetAllFridgeFoods(fridgeId));
    }

    // 5. 단건 조회 (GET /api/fridges/{fridgeId}/foods/{foodId})
    @GetMapping("/{foodId}")
    public ResponseEntity<FridgeFoodDTO> getFridgeFoodById(
            @PathVariable Long fridgeId,
            @PathVariable Long foodId) {

        return fridgeFoodService.svcGetFridgeFoodById(foodId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

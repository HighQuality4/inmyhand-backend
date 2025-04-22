package com.inmyhand.refrigerator.fridge.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.spring.JSONDataView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.domain.dto.SearchFoodCategoryDTO;
import com.inmyhand.refrigerator.category.service.FoodVectorService;
import com.inmyhand.refrigerator.fridge.domain.dto.food.*;
import com.inmyhand.refrigerator.fridge.domain.dto.search.SearchDTO;
import com.inmyhand.refrigerator.fridge.service.FridgeFoodService;
import com.inmyhand.refrigerator.test.TestDTO;
import com.inmyhand.refrigerator.util.ConverterClassUtil;
import com.inmyhand.refrigerator.util.DtoMapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fridge")
@RequiredArgsConstructor
@Slf4j
public class FridgeFoodRestController {

    private final FridgeFoodService fridgeFoodService;
    private final FoodVectorService foodVectorService;

    // 메인 냉장고의 전체 식재료 리스트 조회
    @GetMapping("/main/{memberId}/foods")
    public ResponseEntity<?> getMainFridgeFoods(@PathVariable Long memberId) {
        List<FridgeFoodDTO> foodsInfo = fridgeFoodService.svcGetFridgeMainPage(memberId);
        return ResponseEntity.ok(Map.of("foodsInfo",foodsInfo));
    }

    //-----------------------------------------
    @PostMapping("/change")
    public ResponseEntity<?> getChangeFridgeFoods() {

        Long fridgeId = 2L;
        List<FridgeFoodDTO> foodsList = fridgeFoodService.svcGetAllFridgeFoods(fridgeId);
        Map<String, Object> result = DtoMapperUtils.wrapInMap("foodList", foodsList);

        return ResponseEntity.ok(result);
    }

    // 유저의 참여중인 냉장고 리스트
    @PostMapping("/myFridgeList")
    public ResponseEntity<?> getFridgeList() {

        Long memberId = 3L;
        List<FridgeWithRoleDTO> fridgeList = fridgeFoodService.svcGetFridgeListWithLeaderFlag(memberId);
        Map<String, Object> result = DtoMapperUtils.wrapInMap("fridgeList", fridgeList);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/search")
    public  ResponseEntity<?>  searchByCategoryName(DataRequest dataRequest) {
        SearchDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "searchDataParam",SearchDTO.class);

        List<SearchFoodCategoryDTO> findFoodCategory = foodVectorService.svcCategoryNameContaining(singleClass.getSearchName());
        Map<String, Object> result = DtoMapperUtils.wrapInMap("searchFoodCategoryList", findFoodCategory);

        return  ResponseEntity.ok(result);
    }

    @PostMapping("/search/{keyword}")
    public  ResponseEntity<?>  searchByCategoryName( @PathVariable String keyword) {
        List<SearchFoodCategoryDTO> findFoodCategory = foodVectorService.svcCategoryNameContaining(keyword);

        return  ResponseEntity.ok(findFoodCategory);
    }


    //-----------------------------------------
    // 저장하기
    @PostMapping("/create/dto")
    public ResponseEntity<?> createDtoSend(DataRequest dataRequest) {
        log.info("data={}", dataRequest);
        FridgeFoodDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "sendFridgeFoodDTO",FridgeFoodDTO.class);
        log.info("data={}",singleClass);
        return ResponseEntity.ok(singleClass);
    }

    // 유저의 참여중인 냉장고 리스트 + 권한정보
    @GetMapping("/myFridgeListAndRole/{memberId}")
    public ResponseEntity<List<FridgeWithRolesDTO>> getFridgeListWithRole(@PathVariable Long memberId) {
        List<FridgeWithRolesDTO> fridgeListWithRoles = fridgeFoodService.svcGetFridgeListWithRoles(memberId);
        return ResponseEntity.ok(fridgeListWithRoles);
    }

    //냉장고별 전체 식재료 조회 (GET /api/fridges/{fridgeId}/foods)
    @GetMapping("{/fridgeId}")
    public ResponseEntity<List<FridgeFoodDTO>> getAllFridgeFoods(@PathVariable Long fridgeId) {
        return ResponseEntity.ok(fridgeFoodService.svcGetAllFridgeFoods(fridgeId));
    }
//
//    // 식재료 정보 입력
    @PostMapping("/create/{fridgeId}")
    public ResponseEntity<Void> createFridgeFood(
            @PathVariable Long fridgeId,
            @RequestBody List<FridgeFoodDTO> dtoList) {

        fridgeFoodService.svcCreateFridgeFood(fridgeId,dtoList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


//
//    // 2. 식재료 수정
//    @PutMapping("/{foodId}")
//    public ResponseEntity<Void> updateFridgeFood(
//            @PathVariable Long fridgeId,
//            @PathVariable Long foodId,
//            @RequestBody FridgeFoodDTO dto) {
//
//        dto.setFridgeId(fridgeId); // fridgeId 유지
//        fridgeFoodService.svcUpdateFridgeFood(foodId, dto);
//        return ResponseEntity.ok().build();
//    }
//
//    // 3. 식재료 삭제 (DELETE /api/fridges/{fridgeId}/foods/{foodId})
//    @DeleteMapping("/{foodId}")
//    public ResponseEntity<Void> deleteFridgeFood(
//            @PathVariable Long fridgeId,
//            @PathVariable Long foodId) {
//
//        fridgeFoodService.svcDeleteFridgeFood(foodId);
//        return ResponseEntity.noContent().build();
//    }
//

//
//    // 5. 단건 조회 (GET /api/fridges/{fridgeId}/foods/{foodId})
//    @GetMapping("/{foodId}")
//    public ResponseEntity<FridgeFoodDTO> getFridgeFoodById(
//            @PathVariable Long fridgeId,
//            @PathVariable Long foodId) {
//
//        return fridgeFoodService.svcGetFridgeFoodById(foodId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}

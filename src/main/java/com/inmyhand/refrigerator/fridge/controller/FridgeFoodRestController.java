package com.inmyhand.refrigerator.fridge.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.spring.JSONDataView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmyhand.refrigerator.category.domain.dto.FoodVectorRequestDTO;
import com.inmyhand.refrigerator.category.domain.dto.SearchFoodCategoryDTO;
import com.inmyhand.refrigerator.category.service.FoodVectorService;
import com.inmyhand.refrigerator.fridge.domain.dto.food.*;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeGroupMemberDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.FridgeLeaderDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.group.MyRoleDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.SearchDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.SearchFridgeDTO;
import com.inmyhand.refrigerator.fridge.domain.entity.FridgeEntity;
import com.inmyhand.refrigerator.fridge.service.FridgeFoodService;
import com.inmyhand.refrigerator.fridge.service.FridgeGroupRoleService;
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
    private final FridgeGroupRoleService fridgeGroupRoleService;

    // 메인 냉장고의 전체 식재료 리스트 조회
    @GetMapping("/main/{memberId}/foods")
    public ResponseEntity<?> getMainFridgeFoods(@PathVariable Long memberId) {
        List<FridgeFoodDTO> foodsInfo = fridgeFoodService.svcGetFridgeMainPage(memberId);
        return ResponseEntity.ok(Map.of("foodsInfo",foodsInfo));
    }

    //-----------------------------------------
    // 냉장고별 식재료 결과 출력
    @PostMapping("/change")
    public ResponseEntity<?> getChangeFridgeFoods() {

        Long fridgeId = 1L; // userid = 3L
        List<FridgeFoodDTO> foodsList = fridgeFoodService.svcGetAllFridgeFoods(fridgeId);
        Map<String, Object> result = DtoMapperUtils.wrapInMap("foodList", foodsList);

        return ResponseEntity.ok(result);
    }

//    /api/fridge/change/click
    @PostMapping("/change/click")
    public ResponseEntity<?> getChangeFridgeFoods(DataRequest dataRequest) {

        Long memberId = 3L;
        SearchFridgeDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "fridgeIdParam", SearchFridgeDTO.class);
        System.out.println(" 다른 냉장고 클릭시  결과 값 >>>>>"+ singleClass.getFridgeId());

        Long fridgeId = singleClass.getFridgeId(); // userid = 3L
        List<FridgeFoodDTO> foodsList = fridgeFoodService.svcGetAllFridgeFoods(fridgeId);

        // 로그 찍기
        System.out.println("=== FridgeFoodDTO List ===");
        for (FridgeFoodDTO dto : foodsList) {
            System.out.println("=== FridgeFoodDTO ===");
            System.out.println("냉장고 ID       : " + dto.getFridgeId());
            System.out.println("식품 ID       : " + dto.getId());
            System.out.println("식품명        : " + dto.getFoodName());
            System.out.println("수량          : " + dto.getFoodAmount());
            System.out.println("충전일자      : " + dto.getChargeDate());
            System.out.println("만료일자      : " + dto.getEndDate());
            System.out.println("카테고리      : " + dto.getCategoryName());
            System.out.println("----------------------------");
        }

        Map<String, Object> result = DtoMapperUtils.wrapInMap("foodList", foodsList);

        return ResponseEntity.ok(result);
    }

    // 유저 체크하기
    @PostMapping("/checkRoles")
    public ResponseEntity<?> getFridgeMyRoleCheck(DataRequest dataRequest) {

        Long memberId = 3L;
        SearchFridgeDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "fridgeIdParam", SearchFridgeDTO.class);
        System.out.println(" 다른 냉장고 클릭시  결과 값 >>>>>"+ singleClass.getFridgeId());

        Long fridgeId = singleClass.getFridgeId(); // userid = 3L

        MyRoleDTO myRole = fridgeGroupRoleService.getRolesForUserInFridge(fridgeId,memberId);

        System.out.println("=== 사용자 권한 ===");
        System.out.println("Editor: " + myRole.getEditor());
        System.out.println("Writer: " + myRole.getWriter());

        return ResponseEntity.ok(Map.of("myRole",myRole));

    }

    // 냉장고에 참여중인 유저 목록
    @PostMapping("/members")
    public ResponseEntity<?>getFridgeMembers(DataRequest dataRequest) {

        long fridgeId = 1L;
        List<FridgeGroupMemberDTO> result = fridgeFoodService.getFridgeGroupMembers(fridgeId);

        return ResponseEntity.ok(Map.of("searchGroupList",result));
    }

    // 유저의 참여중인 냉장고 리스트
    @PostMapping("/myFridgeList")
    public ResponseEntity<?> getFridgeList(DataRequest dataRequest) {
        Long memberId = 3L;
        List<FridgeWithRolesDTO> fridgeList = fridgeFoodService.getFridgeListWithRoles(memberId);
        Map<String, Object> result = DtoMapperUtils.wrapInMap("fridgeList", fridgeList);

        return ResponseEntity.ok(result);
    }


    //-----------------------------------------


    // 식재료 일괄 저장하기
    @PostMapping("/create/foodList")
    public ResponseEntity<?> createDtoSend(DataRequest dataRequest) {
//        log.info("data={}", dataRequest.getRequestObject().toString());

        List<FridgeFoodDTO> classList = ConverterClassUtil.getClassList(dataRequest, "insertFoodList",FridgeFoodDTO.class,"yyyyMMdd");
        log.info("data={}",classList);
        FridgeDTO single = ConverterClassUtil.getSingleClass(dataRequest, "fridgeIdParam",FridgeDTO.class);

        long fridgeId = 1L;
        fridgeFoodService.svcCreateFridgeFood(fridgeId,classList);

        return ResponseEntity.ok("저장에 성공했습니다.");
    }


    // 수정 삭제하기 test 해야돼
    @PostMapping("/update/foodList")
    public ResponseEntity<?> updateFoodDtoSend(DataRequest dataRequest) {
//        log.info("data={}",dataRequest.getParameters("updatedList"));

        // ===================== 1. 식재료 정보 수정하는 로직 =====================
        // 수정할 리스트
        List<FridgeFoodDTO> updateList = ConverterClassUtil.getClassList(dataRequest, "updateList",FridgeFoodDTO.class);

        
        // 수정로직
        fridgeFoodService.svcUpdateFridgeFood(updateList);

        // ===================== 2. 식재료 삭제 로직 =====================
        // 삭제할 리스트
        List<FridgeFoodDTO> deleteList = ConverterClassUtil.getClassList(dataRequest, "deleteList",FridgeFoodDTO.class);
        
        // 삭제 로직
        fridgeFoodService.deleteFridgeFoods(deleteList);

        return ResponseEntity.ok("수정에 성공했습니다.");
    }



    //-----------------------
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


    //-----------------------
    // 유저의 참여중인 냉장고 리스트 + 권한정보
    @GetMapping("/myFridgeListAndRole/{memberId}")
    public ResponseEntity<List<FridgeWithRolesDTO>> getFridgeListWithRole(@PathVariable Long memberId) {
        List<FridgeWithRolesDTO> fridgeListWithRoles = fridgeFoodService.getFridgeListWithRoles(memberId);
        return ResponseEntity.ok(fridgeListWithRoles);
    }

    //냉장고별 전체 식재료 조회 (GET /api/fridges/{fridgeId}/foods)
    @GetMapping("{/fridgeId}")
    public ResponseEntity<List<FridgeFoodDTO>> getAllFridgeFoods(@PathVariable Long fridgeId) {
        return ResponseEntity.ok(fridgeFoodService.svcGetAllFridgeFoods(fridgeId));
    }
//


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

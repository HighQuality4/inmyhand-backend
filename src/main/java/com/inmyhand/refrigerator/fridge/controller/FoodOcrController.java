package com.inmyhand.refrigerator.fridge.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.AcceptInviteRequestDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.MemberFridgeFindDTO;
import com.inmyhand.refrigerator.fridge.service.*;
import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.service.MemberService;
import com.inmyhand.refrigerator.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.ParameterGroup;
import com.inmyhand.refrigerator.fridge.domain.dto.food.OcrFoodDTO;
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

    private final FridgeGroupFacadeService fridgeGroupFacadeService;

    private final FridgeGroupInvitationService invitationService;
//    @GetMapping("/search")
//    public ResponseEntity<List<MemberFridgeFindDTO>> searchByName(@RequestParam("name") String namePart) {
//
//        List<MemberFridgeFindDTO> results = fridgeGroupFacadeService.searchByName(namePart);
//        if (results.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(results);
//    }

    @PostMapping("/search")
    public ResponseEntity<List<MemberFridgeFindDTO>> searchByName(
    		 @RequestParam("name") String namePart) {

        List<MemberFridgeFindDTO> results = fridgeGroupFacadeService.searchByName(namePart);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204
        }
        return ResponseEntity.ok(results);             // 200 + JSON body
    }

    // 초대
    @PostMapping("/invite")
    public ResponseEntity<Void> invite(DataRequest dataRequest) {


//        List<MemberFridgeFindDTO> classList = ConverterClassUtil.getClassList(dataRequest, "addGroupMemberParam", MemberFridgeFindDTO.class);

    	MemberFridgeFindDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "addGroupMemberParam", MemberFridgeFindDTO.class);
    	System.out.println("getMemberName" + singleClass.getMemberName());
    	System.out.println("getMemberId" + singleClass.getMemberId());
//        
//        MemberFridgeFindDTO dto = new MemberFridgeFindDTO(
//                5L,                    // memberId
//                "user5@user.com",      // email
//                "user5",               // memberName
//                "user5"                // nickname
//        );
        Long fridgeId = 3L;

        invitationService.inviteMemberToFridge(fridgeId, singleClass);
        return ResponseEntity.ok().build();
    }

    // 수락
//    @PostMapping("/accept")
//    public ResponseEntity<Void> acceptList(
////            @RequestBody List<AcceptInviteRequestDTO> dtos
//    ) {
//
//        List<AcceptInviteRequestDTO> dtos2 = List.of(
//                new AcceptInviteRequestDTO(5L, 3L)
//        );
//
//        invitationService.acceptInvites(dtos2);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptList(DataRequest dataRequest,@AuthenticationPrincipal CustomUserDetails userDetails) {

    	Long userId = userDetails.getUserId();

    	FridgeDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "addInviteParam", FridgeDTO.class);
    	Long memberId = userId;
    	Long fridgeId = singleClass.getFridgeId();

    	// 2. DTO 리스트 생성
    	List<AcceptInviteRequestDTO> dtos2 = List.of(
    	    new AcceptInviteRequestDTO(memberId, fridgeId)
    	);
    	
    	// 3. 콘솔에 찍어보기 (toString() 이 DTO에 구현되어 있어야 편하게 찍힙니다)
    	System.out.println("▶▶▶ AcceptInviteRequestDTO 리스트: " + dtos2);


    	// 4. 서비스 호출
    	invitationService.acceptInvites(dtos2);
    	
        return ResponseEntity.ok().build();
    }

    
    // 거절 - 삭제
    @PostMapping("/delete")
    public ResponseEntity<Void> revokeList(
    ) {
        List<AcceptInviteRequestDTO> dtos2 = List.of(
                new AcceptInviteRequestDTO(5L, 3L)
        );
        invitationService.revokeInvites(dtos2);
        return ResponseEntity.noContent().build();
    }
}

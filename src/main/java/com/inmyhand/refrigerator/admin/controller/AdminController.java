package com.inmyhand.refrigerator.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.service.AdminService;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import com.inmyhand.refrigerator.util.ConverterClassUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final RecipeInfoRepository recipeInfoRepository;
    private final RecipeQueryService recipeQueryService;

    /**
     * key : ad1
     * value : List<MemberEntityDto>
     *
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView() {
        return ResponseEntity.ok(Map.of("getusers", adminService.findAllMembers()));
    }

    /**
     * 유저 업데이트
     * key : ad1
     * value : List<MemberEntityDto>
     *
     * @return
     */
    @PutMapping("/user-update")
    public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView2(DataRequest dataRequest) {

        List<MemberEntityDto> classList = ConverterClassUtil.getClassList(dataRequest, "getusers", MemberEntityDto.class);
        adminService.updateMember(classList);

        return ResponseEntity.ok(Map.of("getusers", adminService.findAllMembers()));
    }

    @GetMapping("/recipe")
    public ResponseEntity<?> userRecipe(@RequestParam(required = false, value = "id") Long id,
                                        @RequestParam(required = false, value = "page", defaultValue = "0") int pageId)
                                        {
    	
    	log.info("id={}", id);
    	log.info("pageId={}", pageId);
    if(id == null) {
    	id = 1L;
    } 


      return ResponseEntity.ok(adminService.findAllAdminRecipeInfo(id, PageRequest.of(pageId, 30)));
    }
}

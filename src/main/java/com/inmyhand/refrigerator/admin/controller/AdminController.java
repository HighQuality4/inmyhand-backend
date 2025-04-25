package com.inmyhand.refrigerator.admin.controller;

import java.util.List;
import java.util.Map;

import com.cleopatra.spring.fileupload.MultipartFileItem;
import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.service.AdminService;
import com.inmyhand.refrigerator.util.ConverterClassUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

	private final AdminService adminService;

	/**
	 * key : ad1 value : List<MemberEntityDto>
	 *
	 * @return
	 */
	@GetMapping("/users")
	public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView() {
		return ResponseEntity
				.ok(Map.of("getusers", adminService.findAllMembers()));
	}

	/**
	 * 유저 업데이트 key : ad1 value : List<MemberEntityDto>
	 *
	 * @return
	 */
	@PutMapping("/user-update")
	public ResponseEntity<Map<String, List<MemberEntityDto>>> adminUserView2(
			DataRequest dataRequest) {

		List<MemberEntityDto> classList = ConverterClassUtil
				.getClassList(dataRequest, "getusers", MemberEntityDto.class);
		adminService.updateMember(classList);

		return ResponseEntity
				.ok(Map.of("getusers", adminService.findAllMembers()));
	}

	@GetMapping("/recipe/{id}")
	public ResponseEntity<?> userRecipe(
			@RequestParam(required = false, value = "page", defaultValue = "0") int pageId,
			@PathVariable("id") Long id) {

		return ResponseEntity.ok(adminService.findAllAdminRecipeInfo(id,
				PageRequest.of(pageId, 30)));
	}

//	@PostMapping("/user/search")
//	public ResponseEntity<?> userSearch(@ModelAttribute MemberCustomQueryDTO memberCustomQueryDTO){
//
//		return ResponseEntity.ok(Map.of("getusers",adminService.findMemberDTOSearch(memberCustomQueryDTO)));
//	}

	@PostMapping("/user/search")
	public ResponseEntity<?> userSearch(@RequestParam(required = false, value = "page", defaultValue = "0") int pageId,
										@RequestParam( required = false, value = "searchName") String keyword) {

		return ResponseEntity.ok(Map.of("getusers",adminService.findMemberDTOSearch2(PageRequest.of(pageId, 30), keyword)));
	}


}

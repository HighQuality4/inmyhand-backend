package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.*;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberRoleEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberRole;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.member.repository.MemberRoleRepository;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeCommentEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeInfoEntity;
import com.inmyhand.refrigerator.recipe.domain.entity.RecipeLikesEntity;
import com.inmyhand.refrigerator.recipe.repository.RecipeCategoryRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeCommentRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeLikesRepository;
import com.inmyhand.refrigerator.recipe.service.RecipeLikeService;
import com.inmyhand.refrigerator.recipe.service.RecipeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
	private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;
	private final RecipeCommentRepository recipeCommentRepository;
	private final RecipeLikesRepository recipeLikesRepository;
	private final RecipeInfoRepository recipeInfoRepository;

    @Override
    public boolean registerLocal(MemberDTO memberDTO) {
    	try {
	        MemberEntity memberEntity = MemberEntity.builder()
	                .memberName(memberDTO.getMemberName())
	                .nickname(memberDTO.getNickname())
	                .password(passwordEncoder.encode(memberDTO.getPassword()))
	                .email(memberDTO.getEmail())
	                .phoneNum(memberDTO.getPhoneNum())
	                .providerId("LOCAL")
	                .status(MemberStatus.active)
	                .build();
			memberRepository.save(memberEntity);

			// 권한 엔티티 생성
			MemberRoleEntity roleEntity = new MemberRoleEntity();
			roleEntity.setMemberEntity(memberEntity);
			roleEntity.setUserRole(MemberRole.freetier); // 양방향 연관관계 설정

			memberRoleRepository.save(roleEntity);
        
	        return true;
	    } catch (Exception e) {
	    	return false;
	    } 
    }

 	@Override
	public List<MyFoodInfoDTO> MyRefreInfo(Long userId) {

		List<Object[]> rawList = memberRepository.findMyRefreInfo(userId);
		System.out.println(rawList.toString());
		LocalDate today = LocalDate.now();

		return rawList.stream().map(row -> {
			String foodName = (String) row[0];
			String expdateStr = (String) row[1]; // "yyyy-MM-dd"
			LocalDate endDate = LocalDate.parse(expdateStr);
			long remain = ChronoUnit.DAYS.between(today, endDate);

			return MyFoodInfoDTO.builder()
					.foodName(foodName)
					.expdate(String.valueOf(remain))  // 남은 일수로 변환된 문자열
					.build();
		}).toList();
	}

	public Page<MyRecipeDTO> getMyOwnRecipeInfo(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return recipeInfoRepository.findByMemberEntity_Id(userId, pageable);
	}

	public Page<MyLikedRecipeDTO> getMyLikeRecipeInfo(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likedAt"));
		return recipeLikesRepository.findByMemberEntity_Id(userId, pageable);
	}

	public Page<MyCommentDTO> getMyCommentedRecipeInfo(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return recipeCommentRepository.findByMemberEntity_Id(userId, pageable);
	}

//    public String getMemberProfileImage(Long memberId) {
//        return memberRepository.findFileUrlsByMemberId(memberId);
//    }
}
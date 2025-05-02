package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.dto.MyFoodInfoDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
        
	        return true;
	    } catch (Exception e) {
	    	return false;
	    } 
    }

 	@Override
	public List<MyFoodInfoDTO> MyRefreInfo(Long userId) {

		List<MyFoodInfoDTO> myFoodInfoDTOList = memberRepository.findMyRefreInfo(userId);

		return myFoodInfoDTOList.stream().map(dto -> {
			LocalDate today = LocalDate.now();
			LocalDate endDate = LocalDate.parse(dto.getExpdate());  // expdate는 "yyyy-MM-dd" 포맷이어야 함
			long remain = ChronoUnit.DAYS.between(today, endDate);
			String remainString = String.valueOf(remain);
			dto.setExpdate(remainString);
			return dto;
		}).toList();
	}

//    public String getMemberProfileImage(Long memberId) {
//        return memberRepository.findFileUrlsByMemberId(memberId);
//    }
}
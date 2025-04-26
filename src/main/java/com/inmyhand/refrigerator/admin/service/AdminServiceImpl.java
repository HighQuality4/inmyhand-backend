package com.inmyhand.refrigerator.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto;
import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.mapper.MemberMapper;
import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final RecipeInfoRepository recipeInfoRepository;
    private final MemberMapper memberMapper;

    /**
     * 멤버 1개 찾기 -> DTO로 반환
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public MemberEntityDto findByMemberOne(Long id) {
        return memberMapper.toDto(
                memberRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new));
    }

    /**
     * 멤버 리스트로 찾기 -> List<DTO>로 반환
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberEntityDto> findByMemberAll() {
        return memberMapper.toDtoList(memberRepository.findAll());
    }

    /**
     * 멤버 수정
     * 업데이트, 삭제(status 변경)
     * @param memberEntityDto
     */
    @Override
    @Transactional
    public void updateMember(List<MemberEntityDto> memberEntityDto) {
        List<MemberEntity> entity = memberMapper.toEntityList(memberEntityDto);
        memberRepository.saveAll(entity);
    }


    /**
     * Repository 에서 DTO로 불러오기
     *
     */
    @Transactional(readOnly = true)
    public List<MemberEntityDto> findAllMembers() {
        return memberRepository.findAllMemberDto();
    }


    /**
     * Member가 작성한 레시피 목록 보기
     * @return
     */
    @Override
    public Page<AdminRecipeInfoDto> findAllAdminRecipeInfo(Long id, Pageable pageable) {
        return recipeInfoRepository.findAdminRecipeInfoUser(id, pageable);
    }


    @Override
    public Page<MemberEntityDto> findMemberDTOSearch(Pageable pageable,MemberCustomQueryDTO memberCustomQueryDTO) {
        return memberRepository.searchMemberWithPaging(memberCustomQueryDTO, pageable);
    }
}
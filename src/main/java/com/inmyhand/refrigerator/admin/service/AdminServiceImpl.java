package com.inmyhand.refrigerator.admin.service;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.admin.mapper.MemberMapper;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import com.inmyhand.refrigerator.recipe.repository.RecipeInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public void updateMember(MemberEntityDto memberEntityDto) {
        MemberEntity entity = memberMapper.toEntity(memberEntityDto);
        memberRepository.save(entity);
    }




}
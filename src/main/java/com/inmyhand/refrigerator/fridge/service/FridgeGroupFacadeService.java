package com.inmyhand.refrigerator.fridge.service;

import com.inmyhand.refrigerator.fridge.domain.dto.group.MyRoleDTO;
import com.inmyhand.refrigerator.fridge.domain.dto.search.MemberFridgeFindDTO;
import com.inmyhand.refrigerator.member.domain.dto.MemberDTO;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.inmyhand.refrigerator.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeGroupFacadeService {

    private final MemberRepository memberRepository;

    // 권한 관리 + 냉장고 그룹원 관리 (그룹원 추가됐을시 : 적용 권한 + 인원 처리)
    public List<MemberFridgeFindDTO> searchByName(String namePart) {
        List<MemberEntity> entities =
                memberRepository.findByMemberNameContainingIgnoreCase(namePart);

        return entities.stream()
                .map(e -> new MemberFridgeFindDTO(
                        e.getId(),
                        e.getEmail(),
                        e.getMemberName(),
                        e.getNickname()
                ))
                .collect(Collectors.toList());
    }

}

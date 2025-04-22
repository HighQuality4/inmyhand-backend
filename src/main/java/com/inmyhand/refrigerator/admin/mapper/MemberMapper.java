package com.inmyhand.refrigerator.admin.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;

@Component
public class MemberMapper {

    /**
     * MemberEntityDto 리스트를 MemberEntity 리스트로 변환합니다.
     * @param dtoList 변환할 DTO 리스트
     * @return 변환된 Entity 리스트
     */
    public List<MemberEntity> toEntityList(List<MemberEntityDto> dtoList) {
        if (dtoList == null) {
            return Collections.emptyList();
        }

        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * DTO -> MemberEntity
     * @param dto
     * @return MemberEntity
     */
    public MemberEntity toEntity(MemberEntityDto dto) {
        MemberEntity entity = new MemberEntity();
        entity.setId(dto.getId());
        entity.setMemberName(dto.getMemberName());
        entity.setEmail(dto.getEmail());
        entity.setNickname(dto.getNickname());
        entity.setRegdate(dto.getRegdate());
        entity.setProviderId(dto.getProviderId());
        entity.setStatus(dto.getStatus());
        entity.setPhoneNum(dto.getPhoneNum());

        return entity;
    }

    /**
     *  MemberEntity -> DTO
     * @param entity
     * @return MemberEntityDto
     */
    public MemberEntityDto toDto(MemberEntity entity) {
        return new MemberEntityDto(
            entity.getId(),
            entity.getMemberName(),
            entity.getEmail(),
            entity.getNickname(),
            entity.getRegdate(),
            entity.getProviderId(),
            entity.getStatus(),
            entity.getPhoneNum()
        );
    }


    /**
     *  List<MemberEntity> -> List<DTO>
     * @param entities
     * @return MemberEntityDto
     */
    public List<MemberEntityDto> toDtoList(List<MemberEntity> entities) {
        return entities.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}

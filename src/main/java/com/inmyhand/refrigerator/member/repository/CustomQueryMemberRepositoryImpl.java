package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.admin.dto.MemberEntityDto;
import com.inmyhand.refrigerator.member.domain.dto.MemberCustomQueryDTO;
import com.inmyhand.refrigerator.member.domain.enums.MemberStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.inmyhand.refrigerator.member.domain.entity.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class CustomQueryMemberRepositoryImpl implements CustomQueryMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * MemberEntity 동적쿼리 (페이징 적용)
     * @param memberCustomQueryDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<MemberEntityDto> searchMemberWithPaging(MemberCustomQueryDTO memberCustomQueryDTO, Pageable pageable) {
        // 데이터 조회 쿼리
        JPAQuery<MemberEntityDto> query = jpaQueryFactory
                .select(Projections.constructor(MemberEntityDto.class,
                        memberEntity.id,
                        memberEntity.memberName,
                        memberEntity.email,
                        memberEntity.nickname,
                        memberEntity.regdate,
                        memberEntity.providerId,
                        memberEntity.status,
                        memberEntity.phoneNum))
                .from(memberEntity)
                .where(
                        nameContains(memberCustomQueryDTO.getName()),
                        emailContains(memberCustomQueryDTO.getEmail()),
                        nickNameContains(memberCustomQueryDTO.getNickname()),
                        statusEq(memberCustomQueryDTO.getCombo())
                )
                .orderBy(memberEntity.id.asc());

        // 페이징 적용
        List<MemberEntityDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(memberEntity.count())
                .from(memberEntity)
                .where(
                        nameContains(memberCustomQueryDTO.getName()),
                        emailContains(memberCustomQueryDTO.getEmail()),
                        nickNameContains(memberCustomQueryDTO.getNickname()),
                        statusEq(memberCustomQueryDTO.getCombo())
                );

        // Page 객체 생성 및 반환
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? memberEntity.memberName.contains(name) : null;
    }

    private BooleanExpression emailContains(String email) {
        return StringUtils.hasText(email) ? memberEntity.email.contains(email) : null;
    }

    private BooleanExpression nickNameContains(String nickName) {
        return StringUtils.hasText(nickName) ? memberEntity.nickname.contains(nickName) : null;
    }

    private BooleanExpression statusEq(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }

        try {
            // 대소문자 구분 없이 처리하기 위해 소문자로 변환
            String formattedStatus = status.toLowerCase();

            if ("active".equals(formattedStatus)) {
                return memberEntity.status.eq(MemberStatus.active);
            } else if ("banned".equals(formattedStatus)) {
                return memberEntity.status.eq(MemberStatus.banned);
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

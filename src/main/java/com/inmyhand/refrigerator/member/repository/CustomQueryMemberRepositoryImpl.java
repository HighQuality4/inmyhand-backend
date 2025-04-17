package com.inmyhand.refrigerator.member.repository;

import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import java.util.List;
import static com.inmyhand.refrigerator.member.domain.entity.QMemberEntity.memberEntity;



@RequiredArgsConstructor
public class CustomQueryMemberRepositoryImpl implements CustomQueryMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;


    /**
     * MemberEntity 동적쿼리
     * @param name
     * @param email
     * @param nickname
     * @return
     */
    @Override
    public List<MemberEntity> searchMember(String name, String email, String nickname) {
        return jpaQueryFactory
                .selectFrom(memberEntity)
                .where(
                        nameContains(name),
                        emailContains(email),
                        nickNameContains(nickname)
                )
                .fetch();
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
}

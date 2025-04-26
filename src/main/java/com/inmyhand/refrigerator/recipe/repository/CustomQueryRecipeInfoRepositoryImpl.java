package com.inmyhand.refrigerator.recipe.repository;

import com.inmyhand.refrigerator.admin.dto.AdminRecipeInfoDto;
import com.inmyhand.refrigerator.recipe.domain.entity.QRecipeInfoEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.inmyhand.refrigerator.recipe.domain.entity.QRecipeInfoEntity.recipeInfoEntity;


@RequiredArgsConstructor
public class CustomQueryRecipeInfoRepositoryImpl implements CustomQueryRecipeInfoRepository{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<AdminRecipeInfoDto> customQueryRecipe(Pageable pageable, String name) {
        // 메인 쿼리 생성
        JPAQuery<AdminRecipeInfoDto> query = jpaQueryFactory
                .select(Projections.constructor(AdminRecipeInfoDto.class,
                        recipeInfoEntity.id,
                        recipeInfoEntity.recipeName,
                        recipeInfoEntity.createdAt,
                        recipeInfoEntity.recipeLikesList.size(),
                        recipeInfoEntity.recipeViewsList.size()))
                .from(recipeInfoEntity)
                .where(recipeNameContains(name))  // 별도의 메서드 사용
                .orderBy(recipeInfoEntity.createdAt.asc());

        // 페이징 처리를 위한 쿼리 실행
        List<AdminRecipeInfoDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 최적화
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(recipeInfoEntity.count())
                .from(recipeInfoEntity)
                .where(recipeNameContains(name));  // 동일한 조건 적용

        // 페이지 결과 반환
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // null 체크를 수행하는 별도의 메서드
    private BooleanExpression recipeNameContains(String name) {
        return (name != null && !name.trim().isEmpty())
                ? recipeInfoEntity.recipeName.contains(name)
                : null;  // null을 반환하면 QueryDSL이 해당 조건을 무시
    }





}

package com.inmyhand.refrigerator.payment.repository;

import com.inmyhand.refrigerator.payment.domain.dto.SubscriptionDTO;
import com.inmyhand.refrigerator.payment.domain.entity.SubscriptionEntity;
import com.inmyhand.refrigerator.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    MemberEntity member = new MemberEntity();

    @Query(value = "SELECT s.sub_id, u.member_name, s.status, s.start_date, s.next_pay_date, sp.subplan_id, sp.plan_name, sp.price, sp.payment_interval, sp.interval_unit, s.created_at " +
            "FROM subscription s " +
            "INNER JOIN subscription_plans sp ON s.member_id = sp.subplan_id " +
            "INNER JOIN member u ON u.member_id = s.member_id " +
            "WHERE s.member_id = :memberId " +
            "ORDER BY s.created_at DESC",
            nativeQuery = true)
    List<Object[]> findSubscriptionDetailsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.inmyhand.refrigerator.payment.domain.dto.SubscriptionDTO(" +
            "CAST(s.id AS string), " +
            "m.memberName, " +
            "s.status, " +
            "s.startDate, " +  // Timestamp를 그대로 Date로 전달
            "s.nextPayDate, " + // Timestamp를 그대로 Date로 전달
            "CAST(sp.id AS string), " +
            "sp.planName, " +
            "CAST(sp.price AS double), " +
            "sp.paymentInterval, " +
            "sp.intervalUnit, " +
            "s.regdate) " +  // Date 타입으로 직접 전달
            "FROM SubscriptionEntity s " +
            "JOIN s.subscriptionPlansEntity sp " +
            "JOIN PaymentEntity p ON p.subscriptionEntity.id = s.id " +
            "JOIN p.memberEntity m " +
            "GROUP BY s.id, m.memberName, s.status, s.startDate, s.nextPayDate, " +
            "sp.id, sp.planName, sp.price, sp.paymentInterval, sp.intervalUnit, s.regdate")
    List<SubscriptionDTO> findAllSubscriptionDTOs();

    // 특정 회원의 구독 정보만 조회
    @Query("SELECT new com.inmyhand.refrigerator.payment.domain.dto.SubscriptionDTO(" +
            "CAST(s.id AS string), " +
            "m.memberName, " +
            "s.status, " +
            "s.startDate, " +
            "s.nextPayDate, " +
            "CAST(sp.id AS string), " +
            "sp.planName, " +
            "CAST(sp.price AS double), " +
            "sp.paymentInterval, " +
            "sp.intervalUnit, " +
            "s.regdate) " +
            "FROM SubscriptionEntity s " +
            "JOIN s.subscriptionPlansEntity sp " +
            "JOIN PaymentEntity p ON p.subscriptionEntity.id = s.id " +
            "JOIN p.memberEntity m " +
            "WHERE m.id = :memberId " +
            "GROUP BY s.id, m.memberName, s.status, s.startDate, s.nextPayDate, " +
            "sp.id, sp.planName, sp.price, sp.paymentInterval, sp.intervalUnit, s.regdate")
    List<SubscriptionDTO> findSubscriptionDTOsByMemberId(@Param("memberId") Long memberId);

}

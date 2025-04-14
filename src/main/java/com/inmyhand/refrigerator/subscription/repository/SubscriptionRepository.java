package com.inmyhand.refrigerator.subscription.repository;

import com.inmyhand.refrigerator.subscription.domain.dto.SubscriptionDetailDTO;
import com.inmyhand.refrigerator.subscription.domain.dto.SubscriptionInfoDTO;
import com.inmyhand.refrigerator.subscription.domain.entity.SubscriptionEntity;
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

//    List<SubscriptionDetailDTO> findSubscriptionDetailsByMemberId(@Param("memberId") Long memberId);
//    List<Object[]> findSubscriptionDetailsByMemberId(@Param("memberId") Long memberId);
}

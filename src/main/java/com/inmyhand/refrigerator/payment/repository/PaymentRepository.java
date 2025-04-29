package com.inmyhand.refrigerator.payment.repository;

import com.inmyhand.refrigerator.payment.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<Object> findByPaymentKey(String paymentKey);


    List<PaymentEntity> findByMemberEntityId(Long memberId);

}

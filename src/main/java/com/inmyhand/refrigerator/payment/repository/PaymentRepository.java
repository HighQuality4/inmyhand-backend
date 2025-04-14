package com.inmyhand.refrigerator.payment.repository;

import com.inmyhand.refrigerator.payment.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

}

package com.inmyhand.refrigerator.payment.repository;

import com.inmyhand.refrigerator.payment.domain.entity.SubscriptionPlansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlansEntityRepository extends JpaRepository<SubscriptionPlansEntity, Long> {
}
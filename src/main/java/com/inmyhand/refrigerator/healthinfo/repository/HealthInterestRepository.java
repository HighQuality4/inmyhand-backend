package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.entity.HealthInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthInterestRepository extends JpaRepository<HealthInterestEntity, Long> {
}

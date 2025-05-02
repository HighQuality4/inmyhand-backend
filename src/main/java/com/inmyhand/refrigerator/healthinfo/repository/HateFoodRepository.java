package com.inmyhand.refrigerator.healthinfo.repository;

import com.inmyhand.refrigerator.healthinfo.domain.entity.HateFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HateFoodRepository extends JpaRepository<HateFoodEntity, Long> {

}

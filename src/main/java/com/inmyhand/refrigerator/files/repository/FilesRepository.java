package com.inmyhand.refrigerator.files.repository;

import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<FilesEntity, Long> {
}

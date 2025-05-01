package com.inmyhand.refrigerator.files.repository;

import com.inmyhand.refrigerator.files.domain.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<FilesEntity, Long> {
    List<FilesEntity> findAllByFileStatus(String fileStatus);

    Collection<Object> findByMemberEntity_IdAndFileStatus(Long memberId, String active);

    Optional<FilesEntity> findByFileUrl(String fileUrl);
}

package com.internship.tool.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;

import java.util.UUID;

@Repository
public interface AuditProgramRepository extends JpaRepository<AuditProgram, UUID> {

    List<AuditProgram> findByStatus(AuditStatus status);

    Page<AuditProgram> findByStatus(AuditStatus status, Pageable pageable);

    Page<AuditProgram> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<AuditProgram> findByStatusAndTitleContainingIgnoreCase(
            AuditStatus status,
            String title,
            Pageable pageable
    );
}

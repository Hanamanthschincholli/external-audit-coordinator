package com.internship.tool.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.internship.tool.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByAuditProgramId(UUID auditProgramId);

    List<Document> findByAuditTaskId(UUID auditTaskId);

    List<Document> findByUploadedById(UUID userId);

    List<Document> findByDocumentTypeIgnoreCase(String documentType);

@Query("SELECT d FROM Document d WHERE d.auditProgram.id = :auditProgramId AND d.confidential = false")
    List<Document> findNonConfidentialByProgramId(@Param("auditProgramId") UUID auditProgramId);
}

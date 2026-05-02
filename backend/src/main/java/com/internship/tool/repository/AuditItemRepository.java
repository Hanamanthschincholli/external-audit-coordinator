package com.internship.tool.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.internship.tool.entity.AuditItem;

@Repository
public interface AuditItemRepository extends JpaRepository<AuditItem, UUID> {

    // Soft delete filter - active items only
    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false")
    Page<AuditItem> findAllActive(Pageable pageable);

    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false AND a.status = :status")
    Page<AuditItem> findActiveByStatus(@Param("status") String status, Pageable pageable);

// Dynamic search/filter method - title is passed as pattern already wrapped with %
    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false " +
           "AND (:titlePattern IS NULL OR LOWER(a.title) LIKE LOWER(:titlePattern)) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:priority IS NULL OR a.priority = :priority) " +
           "AND (:assignedTo IS NULL OR a.assignedTo = :assignedTo)")
    Page<AuditItem> findByFilters(
            @Param("titlePattern") String titlePattern,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("assignedTo") String assignedTo,
            Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE AuditItem a SET a.isDeleted = true WHERE a.id = :id")
    void softDeleteById(@Param("id") UUID id);

}

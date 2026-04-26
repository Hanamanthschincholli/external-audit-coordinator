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

    // ✅ Fixed: deleted instead of isDeleted
    @Query("SELECT a FROM AuditItem a WHERE a.deleted = false")
    Page<AuditItem> findAllActive(Pageable pageable);

<<<<<<< HEAD
    @Query("SELECT a FROM AuditItem a WHERE a.deleted = false AND a.status = :status")
    Page<AuditItem> findActiveByStatus(@Param("status") String status, Pageable pageable);
=======
    @Query("SELECT a FROM AuditItem a WHERE a.isDeleted = false AND a.status = :status")
    Page<AuditItem> findActiveByStatus(@Param("status") String status, Pageable pageable);
   
>>>>>>> 657095f (Added AuditItem CRUD with validation, filtering, and pagination)

    // ✅ Fixed here also
    @Query("SELECT a FROM AuditItem a WHERE a.deleted = false " +
           "AND (:titlePattern IS NULL OR LOWER(a.title) LIKE LOWER(:titlePattern)) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:priority IS NULL OR a.priority = :priority) " +
 "AND (:assignedTo IS NULL OR a.assignedTo.id = :assignedTo)")
    Page<AuditItem> findByFilters(
            @Param("titlePattern") String titlePattern,
            @Param("status") String status,
            @Param("priority") String priority,
@Param("assignedTo") UUID assignedTo,
            Pageable pageable);

    // ✅ Fixed here also
    @Transactional
    @Modifying
    @Query("UPDATE AuditItem a SET a.deleted = true WHERE a.id = :id")
    void softDeleteById(@Param("id") UUID id);
<<<<<<< HEAD
}
=======

}
>>>>>>> 657095f (Added AuditItem CRUD with validation, filtering, and pagination)

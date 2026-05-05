package com.internship.tool.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.internship.tool.entity.AuditStatus;
import com.internship.tool.entity.AuditTask;

import java.util.UUID;

@Repository
public interface AuditTaskRepository extends JpaRepository<AuditTask, UUID> {

    List<AuditTask> findByAuditProgramId(UUID auditProgramId);

    List<AuditTask> findByAssignedToId(UUID userId);

    List<AuditTask> findByAuditProgramIdAndStatus(UUID auditProgramId, AuditStatus status);

    @Query("""
            SELECT t FROM AuditTask t
            WHERE t.assignedTo.id = :userId
              AND t.status NOT IN ('COMPLETED', 'CANCELLED')
            ORDER BY t.dueDate ASC
            """)
    List<AuditTask> findOpenTasksByUser(@Param("userId") UUID userId);

    @Query("""
            SELECT t FROM AuditTask t
            WHERE t.dueDate < :today
              AND t.status NOT IN ('COMPLETED', 'CANCELLED')
            """)
    List<AuditTask> findOverdueTasks(@Param("today") LocalDate today);
}

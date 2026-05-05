package com.internship.tool.repository;

import com.internship.tool.entity.Finding;
import com.internship.tool.entity.FindingSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FindingRepository extends JpaRepository<Finding, UUID> {

    List<Finding> findByAuditProgramId(UUID auditProgramId);

    List<Finding> findByRaisedById(UUID userId);

    List<Finding> findByOwnerId(UUID userId);

    List<Finding> findBySeverity(FindingSeverity severity);

    List<Finding> findByResolvedFalse();

    Optional<Finding> findByReferenceNumber(String referenceNumber);

    @Query("""
            SELECT f FROM Finding f
            WHERE f.auditProgram.id = :programId
              AND f.resolved = false
            ORDER BY
              CASE f.severity
                WHEN 'CRITICAL'      THEN 1
                WHEN 'HIGH'          THEN 2
                WHEN 'MEDIUM'        THEN 3
                WHEN 'LOW'           THEN 4
                WHEN 'INFORMATIONAL' THEN 5
              END
            """)
    List<Finding> findOpenFindingsByProgram(@Param("programId") UUID programId);

    @Query("""
            SELECT f.severity, COUNT(f)
            FROM Finding f
            WHERE f.resolved = false
            GROUP BY f.severity
            """)
    List<Object[]> countOpenFindingsBySeverity();
}

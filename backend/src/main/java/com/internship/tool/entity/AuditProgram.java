package com.internship.tool.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "audit_programs")
public class AuditProgram {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;

    private String departmentUnderAudit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AuditProgram() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AuditStatus getStatus() { return status; }
    public void setStatus(AuditStatus status) { this.status = status; }

    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }

    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }

    public String getDepartmentUnderAudit() { return departmentUnderAudit; }
    public void setDepartmentUnderAudit(String departmentUnderAudit) { this.departmentUnderAudit = departmentUnderAudit; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

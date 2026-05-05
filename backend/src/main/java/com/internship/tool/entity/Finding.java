package com.internship.tool.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "findings")
public class Finding {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private String severity;

    private String recommendation;
    private String managementResponse;
    private String actionPlan;

    private LocalDate targetRemediationDate;
    private LocalDate actualRemediationDate;

    private boolean resolved;

    private String referenceNumber;

    @ManyToOne
    private User raisedBy;

    @ManyToOne
    private User owner;

    @ManyToOne
    private AuditProgram auditProgram;

    public Finding() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getManagementResponse() { return managementResponse; }
    public void setManagementResponse(String managementResponse) { this.managementResponse = managementResponse; }

    public String getActionPlan() { return actionPlan; }
    public void setActionPlan(String actionPlan) { this.actionPlan = actionPlan; }

    public LocalDate getTargetRemediationDate() { return targetRemediationDate; }
    public void setTargetRemediationDate(LocalDate targetRemediationDate) { this.targetRemediationDate = targetRemediationDate; }

    public LocalDate getActualRemediationDate() { return actualRemediationDate; }
    public void setActualRemediationDate(LocalDate actualRemediationDate) { this.actualRemediationDate = actualRemediationDate; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public User getRaisedBy() { return raisedBy; }
    public void setRaisedBy(User raisedBy) { this.raisedBy = raisedBy; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public AuditProgram getAuditProgram() { return auditProgram; }
    public void setAuditProgram(AuditProgram auditProgram) { this.auditProgram = auditProgram; }
}


package com.internship.tool.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuditItemDTO {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String assignedTo;
}

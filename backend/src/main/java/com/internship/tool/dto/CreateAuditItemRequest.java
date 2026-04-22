package com.internship.tool.dto;

import lombok.Data;

@Data
public class CreateAuditItemRequest {
    private String title;
    private String description;
    private String status = "PENDING";
    private String priority = "MEDIUM";
    private String dueDate;
    private String assignedTo;
}

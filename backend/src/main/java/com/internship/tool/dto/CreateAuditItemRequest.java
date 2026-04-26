package com.internship.tool.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateAuditItemRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String status = "PENDING";
    private String priority = "MEDIUM";
    private String dueDate;
    private String assignedTo;
}
package com.internship.tool.mapper;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.entity.AuditItem;

public class AuditItemMapper {

    private AuditItemMapper() {
        // Prevent instantiation
    }

    // ENTITY → DTO
    // ✅ ENTITY → DTO
    public static AuditItemDTO toDTO(AuditItem item) {
        if (item == null) {
            return null;
        }

        AuditItemDTO dto = new AuditItemDTO();

        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setStatus(item.getStatus());
        dto.setPriority(item.getPriority());

        if (item.getDueDate() != null) {
            dto.setDueDate(item.getDueDate());

        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setCreatedBy(item.getCreatedBy() != null ? item.getCreatedBy().getUsername() : null);
        dto.setAssignedTo(item.getAssignedTo() != null ? item.getAssignedTo().getUsername() : null);

        return dto;
    }

        dto.setCreatedBy(item.getCreatedBy());
        dto.setAssignedTo(item.getAssignedTo());

        return dto;
    }
}

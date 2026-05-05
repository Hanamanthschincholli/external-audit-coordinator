package com.internship.tool.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.service.AuditItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/audit-items")
@Tag(name = "Audit Items", description = "Manage audit items (create, update, delete, search)")
public class AuditItemController {

    private final AuditItemService auditItemService;

    public AuditItemController(AuditItemService auditItemService) {
        this.auditItemService = auditItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new audit item")
    public ResponseEntity<AuditItemDTO> createAuditItem(
            @Valid @RequestBody CreateAuditItemRequest request,
            @Parameter(description = "User ID of creator")
            @RequestHeader("X-User-Id") String userIdStr) {

        UUID userId = UUID.fromString(userIdStr);
        return ResponseEntity.ok(
                auditItemService.createAuditItem(request, userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get all audit items with filters and pagination")
    public ResponseEntity<Page<AuditItemDTO>> getAuditItems(
            Pageable pageable,
            @Parameter(description = "Filter by title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) String priority,
            @Parameter(description = "Filter by assigned user") @RequestParam(required = false) String assignedToStr) {

        UUID assignedTo = (assignedToStr != null && !assignedToStr.isEmpty()) ? UUID.fromString(assignedToStr) : null;

        Pageable processedPageable = processPageable(pageable);

        return ResponseEntity.ok(
                auditItemService.searchAuditItems(
                        processedPageable, title, status, priority, assignedTo));
    }

    private Pageable processPageable(Pageable pageable) {
        if (pageable == null) {
            return PageRequest.of(0, 10, Sort.by("title").ascending());
        }

        Sort sort = pageable.getSort();
        if (sort.isUnsorted()) {
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("title").ascending());
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            String property = order.getProperty();

            if ("string".equalsIgnoreCase(property)) {
                property = "title";
            }

            orders.add(new Sort.Order(order.getDirection(), property));
        }

        return PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(orders));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get audit item by ID")
    public ResponseEntity<AuditItemDTO> getAuditItem(
            @Parameter(description = "Audit item ID")
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                auditItemService.getAuditItemById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Update audit item")
    public ResponseEntity<AuditItemDTO> updateAuditItem(
            @Parameter(description = "Audit item ID")
            @PathVariable UUID id,
            @Valid @RequestBody CreateAuditItemRequest request,
            @Parameter(description = "User ID of updater")
            @RequestHeader("X-User-Id") String userIdStr) {

        UUID userId = UUID.fromString(userIdStr);
        return ResponseEntity.ok(
                auditItemService.updateAuditItem(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete audit item (Admin only)")
    public ResponseEntity<Void> deleteAuditItem(
            @Parameter(description = "Audit item ID")
            @PathVariable UUID id) {

        auditItemService.deleteAuditItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get audit items by status")
    public ResponseEntity<Page<AuditItemDTO>> getByStatus(
            @Parameter(description = "Status value")
            @PathVariable String status,
            Pageable pageable) {

        return ResponseEntity.ok(
                auditItemService.getByStatus(status, pageable));
    }
}

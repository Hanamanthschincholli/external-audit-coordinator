package com.internship.tool.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.mapper.AuditItemMapper;
import com.internship.tool.repository.AuditItemRepository;

@Service
public class AuditItemService {

    private final AuditItemRepository auditItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuditItemService.class);

    @Autowired
    public AuditItemService(AuditItemRepository auditItemRepository) {
        this.auditItemRepository = auditItemRepository;
    }

    // ✅ CREATE
    @CacheEvict(value = "auditItems", allEntries = true)
    public AuditItemDTO createAuditItem(CreateAuditItemRequest request, String createdBy) {
        logger.info("Creating audit item with title: {}", request.getTitle());

        AuditItem item = new AuditItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStatus(request.getStatus());
        item.setPriority(request.getPriority());

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            LocalDate dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            item.setDueDate(dueDate);
        }

        item.setCreatedBy(createdBy);
        item.setAssignedTo(request.getAssignedTo());
        item.setDeleted(false);

        AuditItem saved = auditItemRepository.save(item);
        return AuditItemMapper.toDTO(saved);
    }

    // ✅ GET ALL
    @Cacheable("auditItems")
    public Page<AuditItemDTO> getAllAuditItems(Pageable pageable) {
        logger.info("Fetching all audit items");

        return auditItemRepository.findAll(pageable)
                .map(AuditItemMapper::toDTO);
    }

    // ✅ SEARCH
    public Page<AuditItemDTO> searchAuditItems(Pageable pageable,
                                               String title,
                                               String status,
                                               String priority,
                                               String assignedTo) {

        logger.info("Searching audit items with filters");

        return auditItemRepository.findByFilters(title, status, priority, assignedTo, pageable)
                .map(AuditItemMapper::toDTO);
    }

    // ✅ GET BY ID
    public AuditItemDTO getAuditItemById(UUID id) {
        logger.info("Fetching audit item with id: {}", id);

        return auditItemRepository.findById(id)
                .filter(item -> !item.isDeleted())
                .map(AuditItemMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Audit item not found"));
    }

    // ✅ UPDATE
    @CacheEvict(value = "auditItems", allEntries = true)
    public AuditItemDTO updateAuditItem(UUID id, CreateAuditItemRequest request, String updatedBy) {
        logger.info("Updating audit item with id: {}", id);

        AuditItem item = auditItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit item not found"));

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStatus(request.getStatus());
        item.setPriority(request.getPriority());

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            LocalDate dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            item.setDueDate(dueDate);
        }

        item.setAssignedTo(request.getAssignedTo());

        AuditItem saved = auditItemRepository.save(item);
        return AuditItemMapper.toDTO(saved);
    }

    // ✅ DELETE
    @CacheEvict(value = "auditItems", allEntries = true)
    public void deleteAuditItem(UUID id) {
        logger.info("Deleting audit item with id: {}", id);
        auditItemRepository.softDeleteById(id);
    }

    // ✅ FILTER BY STATUS
    public Page<AuditItemDTO> getByStatus(String status, Pageable pageable) {
        logger.info("Fetching audit items by status: {}", status);

        return auditItemRepository.findActiveByStatus(status, pageable)
                .map(AuditItemMapper::toDTO);
    }
}
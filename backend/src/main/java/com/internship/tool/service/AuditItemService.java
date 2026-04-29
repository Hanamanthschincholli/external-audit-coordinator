package com.internship.tool.service
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

<<<<<<< HEAD
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
=======
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)
import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.repository.AuditItemRepository;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.internship.tool.dto.AuditItemDTO;
import com.internship.tool.dto.CreateAuditItemRequest;
import com.internship.tool.entity.AuditItem;
import com.internship.tool.entity.User;
import com.internship.tool.mapper.AuditItemMapper;
import com.internship.tool.repository.AuditItemRepository;
=======
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)

@Service
public class AuditItemService {

    private final AuditItemRepository auditItemRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuditItemService.class);

    @Autowired
    public AuditItemService(AuditItemRepository auditItemRepository, UserService userService) {
        this.auditItemRepository = auditItemRepository;
        this.userService = userService;
    }

    public AuditItemDTO createAuditItem(CreateAuditItemRequest request, UUID createdById) {
        logger.info("Creating audit item with title: {}", request.getTitle());

        User createdUser = userService.getUserById(createdById);
        User assignedUser = request.getAssignedTo() != null ? userService.getUserById(request.getAssignedTo()) : null;

    @Autowired
    public AuditItemService(AuditItemRepository auditItemRepository) {
        this.auditItemRepository = auditItemRepository;
    }

    //  CACHE CLEAR ON CREATE
    @CacheEvict(value = "auditItems", allEntries = true)
    public AuditItemDTO createAuditItem(CreateAuditItemRequest request, String createdBy) {
        AuditItem item = new AuditItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStatus(request.getStatus());
        item.setPriority(request.getPriority());

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            LocalDate dueDate = LocalDate.parse(request.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            item.setDueDate(dueDate);
        }

<<<<<<< HEAD
        item.setCreatedBy(createdUser);
        item.setAssignedTo(assignedUser);
=======
        item.setCreatedBy(createdBy);
        item.setAssignedTo(request.getAssignedTo());
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)
        item.setDeleted(false);

        AuditItem saved = auditItemRepository.save(item);
        return AuditItemMapper.toDTO(saved);
    }

<<<<<<< HEAD
    @Cacheable("auditItems")
    public Page<AuditItemDTO> getAllAuditItems(Pageable pageable) {
        logger.info("Fetching all audit items");

        return auditItemRepository.findAll(pageable)
                .map(AuditItemMapper::toDTO);
    }

    public Page<AuditItemDTO> searchAuditItems(Pageable pageable,
                                               String title,
                                               String status,
                                               String priority,
                                               UUID assignedTo) {

        logger.info("Searching audit items with filters");

        return auditItemRepository.findByFilters(title, status, priority, assignedTo, pageable)
                .map(AuditItemMapper::toDTO);
=======
    //  CACHE READ
    @Cacheable("auditItems")
    public Page<AuditItemDTO> getAllAuditItems(Pageable pageable) {
        return auditItemRepository.findAll(pageable)
                .map(this::mapToDTO);
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)
    }

    public AuditItemDTO getAuditItemById(UUID id) {
        logger.info("Fetching audit item with id: {}", id);

        return auditItemRepository.findById(id)
                .filter(item -> !item.isDeleted())
                .map(AuditItemMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Audit item not found"));
    }

<<<<<<< HEAD
    @CacheEvict(value = "auditItems", allEntries = true)
    public AuditItemDTO updateAuditItem(UUID id, CreateAuditItemRequest request, UUID updatedById) {
        logger.info("Updating audit item with id: {}", id);

=======
    // CACHE CLEAR ON UPDATE
    @CacheEvict(value = "auditItems", allEntries = true)
    public AuditItemDTO updateAuditItem(UUID id, CreateAuditItemRequest request, String updatedBy) {
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)
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

<<<<<<< HEAD
        if (request.getAssignedTo() != null) {
            User assignedUser = userService.getUserById(request.getAssignedTo());
            item.setAssignedTo(assignedUser);
        }
=======
        item.setAssignedTo(request.getAssignedTo());
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)

        AuditItem saved = auditItemRepository.save(item);
        return AuditItemMapper.toDTO(saved);
    }

<<<<<<< HEAD
=======
    //  CACHE CLEAR ON DELETE
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)
    @CacheEvict(value = "auditItems", allEntries = true)
    public void deleteAuditItem(UUID id) {
        logger.info("Deleting audit item with id: {}", id);
        auditItemRepository.softDeleteById(id);
    }

    public Page<AuditItemDTO> getByStatus(String status, Pageable pageable) {

        logger.info("Fetching audit items by status: {}", status);
        return auditItemRepository.findActiveByStatus(status, pageable)
                .map(this::mapToDTO);
    }

        return auditItemRepository.findActiveByStatus(status, pageable)
                .map(AuditItemMapper::toDTO);
    }
<<<<<<< HEAD
}

=======
}
>>>>>>> 242ab10 (Add caching to audit items using Spring Cache)

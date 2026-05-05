package com.internship.tool.service;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditTask;
import com.internship.tool.entity.Document;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.DocumentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuditProgramService auditProgramService;
    private final AuditTaskService auditTaskService;
    private final UserService userService;

    public DocumentService(DocumentRepository documentRepository, 
                          AuditProgramService auditProgramService,
                          AuditTaskService auditTaskService,
                          UserService userService) {
        this.documentRepository = documentRepository;
        this.auditProgramService = auditProgramService;
        this.auditTaskService = auditTaskService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "documents", key = "#id")
    public Document getDocumentById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsByProgramId(UUID programId) {
        return documentRepository.findByAuditProgramId(programId);
    }

    @Transactional(readOnly = true)
    public Page<Document> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Document> getDocumentsByTaskId(UUID taskId) {
        return documentRepository.findByAuditTaskId(taskId);
    }

    @Transactional
    @CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public Document uploadDocument(Document document, UUID uploaderId, UUID programId, UUID taskId) {
        if (programId == null && taskId == null) {
            throw new ValidationException("Document must be associated with either an AuditProgram or an AuditTask.");
        }

        User uploader = userService.getUserById(uploaderId);
        document.setUploadedBy(uploader);

        if (programId != null) {
            AuditProgram program = auditProgramService.getProgramById(programId);
            document.setAuditProgram(program);
        }

        if (taskId != null) {
            AuditTask task = auditTaskService.getTaskById(taskId);
            document.setAuditTask(task);
            if (document.getAuditProgram() == null && task.getAuditProgram() != null) {
                document.setAuditProgram(task.getAuditProgram());
            }
        }

        return documentRepository.save(document);
    }

    @Transactional
    @CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public Document updateDocumentMetadata(UUID id, Document documentDetails) {
        Document document = getDocumentById(id);
        document.setDescription(documentDetails.getDescription());
        document.setDocumentType(documentDetails.getDocumentType());
        document.setConfidential(documentDetails.isConfidential());
        return documentRepository.save(document);
    }

    @Transactional
    @CacheEvict(value = {"documents", "documentsList", "documentsPaginated"}, allEntries = true)
    public void deleteDocument(UUID id) {
        Document document = getDocumentById(id);
        documentRepository.delete(document);
    }
}

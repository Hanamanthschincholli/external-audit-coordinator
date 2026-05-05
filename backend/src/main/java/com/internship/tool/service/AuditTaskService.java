package com.internship.tool.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import com.internship.tool.entity.AuditTask;
import com.internship.tool.entity.User;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.AuditTaskRepository;

import java.util.UUID;

@Service
public class AuditTaskService {

    private final AuditTaskRepository auditTaskRepository;
    private final AuditProgramService auditProgramService;
    private final UserService userService;

    public AuditTaskService(AuditTaskRepository auditTaskRepository,
                           AuditProgramService auditProgramService,
                           UserService userService) {
        this.auditTaskRepository = auditTaskRepository;
        this.auditProgramService = auditProgramService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tasks", key = "#id")
    public AuditTask getTaskById(UUID id) {
        return auditTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditTask", "id", id));
    }

    @Transactional(readOnly = true)
    public List<AuditTask> getTasksByProgramId(UUID programId) {
        return auditTaskRepository.findByAuditProgramId(programId);
    }

    @Transactional(readOnly = true)
    public Page<AuditTask> getAllTasks(Pageable pageable) {
        return auditTaskRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<AuditTask> getTasksByAssigneeId(UUID assigneeId) {
        return auditTaskRepository.findByAssignedToId(assigneeId);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask createTask(UUID programId, UUID assigneeId, AuditTask task) {
        AuditProgram program = auditProgramService.getProgramById(programId);
        task.setAuditProgram(program);

        if (assigneeId != null) {
            User assignee = userService.getUserById(assigneeId);
            task.setAssignedTo(assignee);
        }

        if (task.getStatus() == null) {
            task.setStatus(AuditStatus.PLANNED);
        }

        return auditTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask updateTask(UUID id, AuditTask taskDetails) {
        AuditTask task = getTaskById(id);

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setPriority(taskDetails.getPriority());
        task.setNotes(taskDetails.getNotes());

        return auditTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask updateTaskStatus(UUID id, AuditStatus newStatus) {
        AuditTask task = getTaskById(id);
        
        if (newStatus == AuditStatus.COMPLETED && task.getCompletedDate() == null) {
            task.setCompletedDate(LocalDate.now());
        }
        
        task.setStatus(newStatus);
        return auditTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public AuditTask assignTask(UUID taskId, UUID assigneeId) {
        AuditTask task = getTaskById(taskId);
        User assignee = userService.getUserById(assigneeId);
        task.setAssignedTo(assignee);
        return auditTaskRepository.save(task);
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList", "tasksPaginated"}, allEntries = true)
    public void deleteTask(UUID id) {
        AuditTask task = getTaskById(id);
        auditTaskRepository.delete(task);
    }
}

package com.internship.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import com.internship.tool.repository.AuditProgramRepository;

import java.util.UUID;

@Service
public class AuditProgramService {

    private final AuditProgramRepository repository;

    public AuditProgramService(AuditProgramRepository repository) {
        this.repository = repository;
    }

    public Page<AuditProgram> getAllPrograms(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public AuditProgram getProgramById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public AuditProgram createProgram(AuditProgram program) {
        program.setCreatedAt(java.time.LocalDateTime.now());
        program.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(program);
    }

    public AuditProgram updateProgram(UUID id, AuditProgram updated) {
        AuditProgram existing = getProgramById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        return repository.save(existing);
    }

    public AuditProgram updateProgramStatus(UUID id, AuditStatus status) {
        AuditProgram program = getProgramById(id);
        program.setStatus(status);
        program.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(program);
    }

    public void deleteProgram(UUID id) {
        repository.deleteById(id);
    }

    public List<AuditProgram> getByStatus(String status) {
        AuditStatus statusEnum = AuditStatus.valueOf(status.toUpperCase());
        return repository.findByStatus(statusEnum);
    }
}

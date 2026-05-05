package com.internship.tool.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.internship.tool.entity.AuditProgram;
import com.internship.tool.entity.AuditStatus;
import com.internship.tool.service.AuditProgramService;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/audit-programs")
public class AuditProgramController {

    private final AuditProgramService auditProgramService;

    public AuditProgramController(AuditProgramService auditProgramService) {
        this.auditProgramService = auditProgramService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AuditProgram>> getAllPrograms(Pageable pageable) {
        Page<AuditProgram> programs = auditProgramService.getAllPrograms(pageable);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditProgram> getProgramById(@PathVariable UUID id) {
        AuditProgram program = auditProgramService.getProgramById(id);
        return ResponseEntity.ok(program);
    }

    @PostMapping("/create")
    public ResponseEntity<AuditProgram> createProgram(@Valid @RequestBody AuditProgram program) {
        AuditProgram createdProgram = auditProgramService.createProgram(program);
        return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditProgram> updateProgram(@PathVariable UUID id, @Valid @RequestBody AuditProgram programDetails) {
        AuditProgram updatedProgram = auditProgramService.updateProgram(id, programDetails);
        return ResponseEntity.ok(updatedProgram);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AuditProgram> updateProgramStatus(@PathVariable UUID id, @RequestParam AuditStatus status) {
        AuditProgram updatedProgram = auditProgramService.updateProgramStatus(id, status);
        return ResponseEntity.ok(updatedProgram);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable UUID id) {
        auditProgramService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}


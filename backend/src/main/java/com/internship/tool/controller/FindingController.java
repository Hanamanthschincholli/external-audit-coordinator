package com.internship.tool.controller;

import com.internship.tool.entity.Finding;
import com.internship.tool.service.FindingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/findings")
public class FindingController {

    private final FindingService findingService;

    public FindingController(FindingService findingService) {
        this.findingService = findingService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Finding>> getAllFindings(Pageable pageable) {
        Page<Finding> findings = findingService.getAllFindings(pageable);
        return ResponseEntity.ok(findings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Finding> getFindingById(@PathVariable UUID id) {
        Finding finding = findingService.getFindingById(id);
        return ResponseEntity.ok(finding);
    }

    @PostMapping("/create")
    public ResponseEntity<Finding> createFinding(
            @Valid @RequestBody Finding finding,
            @RequestParam UUID programId,
            @RequestParam UUID raisedById) {
        Finding createdFinding = findingService.createFinding(programId, raisedById, finding);
        return new ResponseEntity<>(createdFinding, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Finding> updateFinding(@PathVariable UUID id, @Valid @RequestBody Finding findingDetails) {
        Finding updatedFinding = findingService.updateFinding(id, findingDetails);
        return ResponseEntity.ok(updatedFinding);
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Finding> resolveFinding(@PathVariable UUID id) {
        Finding resolvedFinding = findingService.resolveFinding(id);
        return ResponseEntity.ok(resolvedFinding);
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Finding> assignOwner(@PathVariable UUID id, @RequestParam UUID ownerId) {
        Finding updatedFinding = findingService.assignOwner(id, ownerId);
        return ResponseEntity.ok(updatedFinding);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinding(@PathVariable UUID id) {
        findingService.deleteFinding(id);
        return ResponseEntity.noContent().build();
    }
}


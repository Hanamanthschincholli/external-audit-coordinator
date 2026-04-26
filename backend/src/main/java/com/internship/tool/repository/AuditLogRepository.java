package com.internship.tool.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.internship.tool.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
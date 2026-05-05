-- ============================================================
--  FINAL CLEAN SCHEMA (UUID VERSION)
-- ============================================================

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ─── 1. USERS ────────────────────────────────────────────────
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    department VARCHAR(100),
    phone_number VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN','MANAGER','VIEWER'))
);

-- ─── 2. AUDIT_PROGRAMS ───────────────────────────────────────
CREATE TABLE audit_programs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    scope TEXT,
    objectives TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    planned_start_date DATE NOT NULL,
    planned_end_date DATE NOT NULL,
    actual_start_date DATE,
    actual_end_date DATE,
    department_under_audit VARCHAR(100),
    is_external BOOLEAN NOT NULL DEFAULT TRUE,
    lead_auditor_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_ap_status CHECK (
        status IN ('PLANNED','IN_PROGRESS','UNDER_REVIEW','COMPLETED','CANCELLED')
    ),
    FOREIGN KEY (lead_auditor_id) REFERENCES users(id)
);

-- ─── 3. AUDIT_TASKS ──────────────────────────────────────────
CREATE TABLE audit_tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    due_date DATE,
    completed_date DATE,
    priority VARCHAR(10),
    notes TEXT,
    audit_program_id UUID NOT NULL,
    assigned_to_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_at_status CHECK (
        status IN ('PLANNED','IN_PROGRESS','UNDER_REVIEW','COMPLETED','CANCELLED')
    ),
    FOREIGN KEY (audit_program_id) REFERENCES audit_programs(id),
    FOREIGN KEY (assigned_to_id) REFERENCES users(id)
);

-- ─── 4. DOCUMENTS ────────────────────────────────────────────
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    storage_path VARCHAR(512) NOT NULL,
    description TEXT,
    document_type VARCHAR(50),
    is_confidential BOOLEAN NOT NULL DEFAULT FALSE,
    audit_program_id UUID,
    audit_task_id UUID,
    uploaded_by_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (audit_program_id) REFERENCES audit_programs(id),
    FOREIGN KEY (audit_task_id) REFERENCES audit_tasks(id),
    FOREIGN KEY (uploaded_by_id) REFERENCES users(id)
);

-- ─── 5. FINDINGS ─────────────────────────────────────────────
CREATE TABLE findings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    recommendation TEXT,
    management_response TEXT,
    action_plan TEXT,
    target_remediation_date DATE,
    actual_remediation_date DATE,
    is_resolved BOOLEAN NOT NULL DEFAULT FALSE,
    reference_number VARCHAR(50) UNIQUE,
    audit_program_id UUID NOT NULL,
    raised_by_id UUID NOT NULL,
    owner_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_findings_severity CHECK (
        severity IN ('CRITICAL','HIGH','MEDIUM','LOW','INFORMATIONAL')
    ),
    FOREIGN KEY (audit_program_id) REFERENCES audit_programs(id),
    FOREIGN KEY (raised_by_id) REFERENCES users(id),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- ─── 6. NOTIFICATIONS ────────────────────────────────────────
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    resource_type VARCHAR(50),
    resource_id UUID,
    recipient_id UUID NOT NULL,
    sender_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (recipient_id) REFERENCES users(id),
    FOREIGN KEY (sender_id) REFERENCES users(id)
);

-- ─── INDEXES ────────────────────────────────────────────────
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

CREATE INDEX idx_ap_status ON audit_programs(status);
CREATE INDEX idx_ap_lead_auditor ON audit_programs(lead_auditor_id);

CREATE INDEX idx_at_program ON audit_tasks(audit_program_id);
CREATE INDEX idx_at_assignee ON audit_tasks(assigned_to_id);
CREATE INDEX idx_at_status ON audit_tasks(status);

CREATE INDEX idx_doc_program ON documents(audit_program_id);
CREATE INDEX idx_doc_task ON documents(audit_task_id);

CREATE INDEX idx_find_program ON findings(audit_program_id);
CREATE INDEX idx_find_severity ON findings(severity);

CREATE INDEX idx_notif_recipient ON notifications(recipient_id);
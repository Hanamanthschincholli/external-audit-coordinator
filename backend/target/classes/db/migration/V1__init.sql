CREATE TABLE audit_item (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50),
    due_date TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    created_by VARCHAR(100),
    assigned_to VARCHAR(100),

    is_deleted BOOLEAN DEFAULT FALSE
);

-- Indexes for performance
CREATE INDEX idx_status ON audit_item(status);
CREATE INDEX idx_due_date ON audit_item(due_date);
CREATE INDEX idx_created_by ON audit_item(created_by);

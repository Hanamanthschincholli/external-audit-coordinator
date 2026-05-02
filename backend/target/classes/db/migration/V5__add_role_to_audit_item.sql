-- Add role column to audit_item table
ALTER TABLE audit_item ADD COLUMN role VARCHAR(50);

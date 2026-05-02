-- Remove duplicate admin users keeping only the one with fixed UUID
DELETE FROM users WHERE username = 'admin' AND id != '550e8400-e29b-41d4-a716-446655440001'::uuid;


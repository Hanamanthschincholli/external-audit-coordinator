INSERT INTO users (id, username, password, role) 
VALUES ('550e8400-e29b-41d4-a716-446655440001'::uuid, 'admin', '$2a$10$Bxzi/tJ8GOsoF9QxRVlpSOatp6UZn7Wx8Dd3GbDLpkpfY.hWrWAry', 'ADMIN')
ON CONFLICT (id) DO NOTHING;


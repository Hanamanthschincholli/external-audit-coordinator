INSERT INTO users (
    full_name,
    email,
    password_hash,
    role,
    created_at,
    updated_at
)
VALUES (
    'Admin User',
    'admin@example.com',
    '$2a$10$Bxzi/tJ8GOsoF9QxRVlpSOatp6UZn7Wx8Dd3GbDLpkpfY.hWrWAry',
    'ADMIN',
    NOW(),
    NOW()
)
ON CONFLICT (email) DO NOTHING;

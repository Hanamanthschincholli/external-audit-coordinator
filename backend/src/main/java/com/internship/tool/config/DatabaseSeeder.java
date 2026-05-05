package com.internship.tool.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN.toString());
            admin.setActive(true);
            userRepository.save(admin);

            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@example.com");
            manager.setPasswordHash(passwordEncoder.encode("manager123"));
            manager.setRole(Role.MANAGER.toString());
            manager.setActive(true);
            userRepository.save(manager);

            User auditor = new User();
            auditor.setUsername("auditor");
            auditor.setEmail("auditor@example.com");
            auditor.setPasswordHash(passwordEncoder.encode("auditor123"));
            auditor.setRole(Role.AUDITOR.toString());
            auditor.setActive(true);
            userRepository.save(auditor);

            System.out.println("Database seeded successfully with initial users.");
        }
    }
}
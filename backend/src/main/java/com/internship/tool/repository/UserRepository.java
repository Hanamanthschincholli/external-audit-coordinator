package com.internship.tool.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.internship.tool.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE LOWER(TRIM(username)) = LOWER(TRIM(:username))")
    Optional<User> findByUsernameIgnoreCase(@Param("username") String username);
}

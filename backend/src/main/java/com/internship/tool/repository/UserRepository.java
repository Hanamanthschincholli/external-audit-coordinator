package com.internship.tool.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.internship.tool.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("username") String username, @Param("email") String email);
}

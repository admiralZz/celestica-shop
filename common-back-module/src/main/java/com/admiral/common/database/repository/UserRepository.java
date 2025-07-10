package com.admiral.common.database.repository;

import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByRole(UserRole role);
} 
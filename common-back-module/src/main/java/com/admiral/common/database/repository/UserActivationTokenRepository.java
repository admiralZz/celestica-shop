package com.admiral.common.database.repository;

import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserActivationTokenRepository extends JpaRepository<UserActivationToken, Long> {
    Optional<UserActivationToken> findByToken(String token);
    List<UserActivationToken> findByUser(User user);
}
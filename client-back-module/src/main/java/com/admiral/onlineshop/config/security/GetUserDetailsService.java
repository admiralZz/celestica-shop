package com.admiral.onlineshop.config.security;

import com.admiral.common.exception.UserAccessDeniedException;
import com.admiral.common.exception.UserNotFoundException;
import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserRole;
import com.admiral.common.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        if (!user.isEnabled()) {
            throw new UserAccessDeniedException("User doesn't have the activation");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(user.getRole())
        );
    }
} 
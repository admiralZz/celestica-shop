package com.admiral.onlineshop.service;

import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserActivationToken;
import com.admiral.common.database.model.UserRole;
import com.admiral.common.database.repository.UserActivationTokenRepository;
import com.admiral.common.database.repository.UserRepository;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.mapper.UserMapper;
import com.admiral.onlineshop.event.UserActivationEvent;
import com.admiral.onlineshop.exception.UserActivationTokenException;
import com.admiral.onlineshop.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationUsersServiceImpl implements RegistrationUsersService {

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserActivationTokenRepository userActivationTokenRepository;
    private final UserMapper userMapper;

    @Value("${app.user.registration.activation-token.expiration:86400000}")
    private long validityInMilliseconds;

    @Override
    @Transactional
    public ReadUserDTO register(CreateUserDTO createUserDTO) {
        log.debug("Request for registration by email: {}", createUserDTO.getEmail());
        Optional<User> maybeUser = userRepository.findByEmail(createUserDTO.getEmail());
        if (maybeUser.isPresent()) {
            return handleExistingUser(maybeUser.get());
        }

        var savedUser = createNewUser(createUserDTO);
        createTokenAndSendActivationEmail(savedUser);
        log.info("Activation email has been sent to {}", savedUser.getEmail());

        // Преобразование обратно в DTO
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public ReadUserDTO confirm(String token) throws UserActivationTokenException {
        log.debug("Request for activation by token: {}", token);
        var activationToken = userActivationTokenRepository.findByToken(token)
                .orElseThrow(() -> new UserActivationTokenException("Activation token is not found"));
        if (activationToken.isUsed()) {
            throw new UserActivationTokenException("Activation token is already used");
        }

        if (LocalDateTime.now().isAfter(activationToken.getExpiryDate())) {
            throw new UserActivationTokenException("Activation token is expired");
        }

        var user = activationToken.getUser();
        user.setEnabled(true);
        activationToken.setUsed(true);

        userActivationTokenRepository.save(activationToken);
        log.info("The email {} has been activated", user.getEmail());

        return userMapper.toDTO(userRepository.save(user));
    }

    private User createNewUser(CreateUserDTO createUserDTO) {
        // Преобразование DTO в Entity
        User user = userMapper.toEntity(createUserDTO);
        // Хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Установка роли по умолчанию
        user.setRole(UserRole.ROLE_USER);

        // Сохранение в базу данных
        return userRepository.save(user);
    }

    private ReadUserDTO handleExistingUser(User user) {
        if (user.isEnabled()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        List<UserActivationToken> tokens = userActivationTokenRepository.findByUser(user);
        if (tokens.isEmpty()) {
            throw new IllegalStateException("Activation token is not found for non-enabled user");
        }

        Optional<UserActivationToken> usedToken = tokens.stream()
                .filter(UserActivationToken::isUsed)
                .findFirst();
        if (usedToken.isPresent()) {
            throw new IllegalStateException("User activation token is already used");
        }

        Optional<UserActivationToken> freshToken = tokens.stream()
                .filter(token -> LocalDateTime.now().isBefore(token.getExpiryDate()))
                .findFirst();
        if (freshToken.isPresent()) {
            log.info("Resending activation email to {}", user.getEmail());
            sendActivationEmail(freshToken.get());
        } else {
            log.info("Create new token and resending activation email to {}", user.getEmail());
            createTokenAndSendActivationEmail(user);
        }
        // Преобразование обратно в DTO
        return userMapper.toDTO(user);
    }

    private void createTokenAndSendActivationEmail(User user) {
        sendActivationEmail(createActivationToken(user));
    }

    private void sendActivationEmail(UserActivationToken activationToken) {
        // Отправляем email письмо с ссылкой на активацию
        eventPublisher.publishEvent(new UserActivationEvent(activationToken));
    }

    private UserActivationToken createActivationToken(User user) {
        String token = UUID.randomUUID().toString();
        UserActivationToken verificationToken = new UserActivationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plus(validityInMilliseconds, ChronoUnit.MILLIS));
        return userActivationTokenRepository.save(verificationToken);
    }
}

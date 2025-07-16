package com.admiral.onlineshop.integration;

import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserActivationToken;
import com.admiral.common.database.repository.UserActivationTokenRepository;
import com.admiral.common.database.repository.UserRepository;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.service.MailService;
import com.admiral.onlineshop.exception.UserActivationTokenException;
import com.admiral.onlineshop.integration.conf.MockMailServiceConfiguration;
import com.admiral.onlineshop.service.RegistrationUsersService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Import(MockMailServiceConfiguration.class)
@RequiredArgsConstructor
public class RegistrationUsersServiceTest extends IntegrationTest {
    private final RegistrationUsersService registrationUsersService;
    private final UserRepository userRepository;
    private final UserActivationTokenRepository tokenRepository;
    private final MailService mailService;

    @Test
    void registerUser_sendsActivationEmailAndCreatesUser() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("testreg@example.com")
                .password("password123")
                .build();
        ReadUserDTO result = registrationUsersService.register(dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("testreg@example.com", result.getEmail());
        // т.к. отправка мыла происходит асинхронно(@Async), то
        // ждём до 2 секунд, пока не вызовется метод
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        Mockito.verify(mailService, Mockito.atLeastOnce())
                                .sendEmail(Mockito.anyString(), Mockito.eq("testreg@example.com"), Mockito.anyString(), Mockito.anyString())
                );
    }

    @Test
    void confirmUser_activatesUser() {
        // Arrange: регистрируем пользователя
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("testconfirm@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        User user = userRepository.findByEmail("testconfirm@example.com").orElseThrow();
        Assertions.assertFalse(user.isEnabled());
        UserActivationToken token = getFreshTokenByUser(user).orElseThrow();
        Assertions.assertFalse(token.isUsed());
        // Юзер перешел поссылке, попал на фронт, а откуда пришел запрос на подтверждение
        ReadUserDTO confirmed = registrationUsersService.confirm(token.getToken());
        Assertions.assertNotNull(confirmed);
        Assertions.assertEquals("testconfirm@example.com", confirmed.getEmail());
        User activated = userRepository.findByEmail("testconfirm@example.com").orElseThrow();
        Assertions.assertTrue(activated.isEnabled());
        UserActivationToken usedToken = getFreshTokenByUser(user).orElseThrow();
        Assertions.assertTrue(usedToken.isUsed());
    }

    @Test
    void registerUser_withExistingEmailAndEnabled_throwsException() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("enabled@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        // Активируем пользователя вручную
        User user = userRepository.findByEmail("enabled@example.com").orElseThrow();
        UserActivationToken token = getFreshTokenByUser(user).orElseThrow();
        ReadUserDTO confirmed = registrationUsersService.confirm(token.getToken());
        Assertions.assertNotNull(confirmed);
        Assertions.assertEquals("enabled@example.com", confirmed.getEmail());
        // Повторная регистрация
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                registrationUsersService.register(dto));
        Assertions.assertTrue(ex.getMessage().contains("Email already exists"));
    }

    @Test
    void registerUser_withExistingEmailAndNotEnabled_tokenNotExpired_resendsEmail() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("resend@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        // Повторная регистрация до истечения токена
        ReadUserDTO result = registrationUsersService.register(dto);
        Assertions.assertEquals("resend@example.com", result.getEmail());
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        Mockito.verify(mailService, Mockito.atLeast(2))
                                .sendEmail(Mockito.anyString(), Mockito.eq("resend@example.com"), Mockito.anyString(), Mockito.anyString())
                );
    }

    @Test
    void registerUser_withExistingEmailAndNotEnabled_tokenExpired_createsNewTokenAndResendsEmail() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("expired@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        User user = userRepository.findByEmail("expired@example.com").orElseThrow();
        UserActivationToken token = getFreshTokenByUser(user).orElseThrow();
        // Истекает токен
        token.setExpiryDate(token.getExpiryDate().minusYears(1));
        tokenRepository.save(token);
        // Повторная регистрация — должен быть новый токен
        ReadUserDTO result = registrationUsersService.register(dto);
        Assertions.assertEquals("expired@example.com", result.getEmail());
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        Mockito.verify(mailService, Mockito.atLeast(2))
                                .sendEmail(Mockito.anyString(), Mockito.eq("expired@example.com"), Mockito.anyString(), Mockito.anyString())
                );
        // Проверяем, что токен обновился
        UserActivationToken newToken = getFreshTokenByUser(user).orElseThrow();
        Assertions.assertNotEquals(token.getToken(), newToken.getToken());
    }

    @Test
    void confirmUser_withInvalidToken_throwsException() {
        var ex = Assertions.assertThrows(UserActivationTokenException.class, () ->
                registrationUsersService.confirm("invalid-token-123"));
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    }

    @Test
    void confirmUser_withExpiredToken_throwsException() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("expiredtoken@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        User user = userRepository.findByEmail("expiredtoken@example.com").orElseThrow();
        UserActivationToken token = getFreshTokenByUser(user).orElseThrow();
        token.setExpiryDate(token.getExpiryDate().minusYears(1));
        tokenRepository.save(token);
        var ex = Assertions.assertThrows(UserActivationTokenException.class, () ->
                registrationUsersService.confirm(token.getToken()));
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("expired"));
    }

    @Test
    void confirmUser_withUsedToken_throwsException() {
        CreateUserDTO dto = CreateUserDTO.builder()
                .email("usedtoken@example.com")
                .password("password123")
                .build();
        registrationUsersService.register(dto);
        User user = userRepository.findByEmail("usedtoken@example.com").orElseThrow();
        UserActivationToken token = getFreshTokenByUser(user).orElseThrow();
        // Используем токен
        registrationUsersService.confirm(token.getToken());
        // Повторное подтверждение
        var ex = Assertions.assertThrows(UserActivationTokenException.class, () ->
                registrationUsersService.confirm(token.getToken()));
        Assertions.assertTrue(ex.getMessage().toLowerCase().contains("already used"));
    }

    private Optional<UserActivationToken> getFreshTokenByUser(User user) {
        List<UserActivationToken> tokens = tokenRepository.findByUser(user);
        if (tokens.isEmpty()) {
            throw new IllegalStateException("Activation token is not found for non-enabled user");
        }

        return tokens.stream()
                .filter(token -> LocalDateTime.now().isBefore(token.getExpiryDate()))
                .findFirst();
    }
} 
package com.admiral.onlineshop.controller;

import com.admiral.onlineshop.config.security.JwtTokenProvider;
import com.admiral.onlineshop.dto.JwtResponseDTO;
import com.admiral.common.dto.LoginRequestDTO;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.service.UserService;
import com.admiral.onlineshop.service.RegistrationUsersService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RegistrationUsersService registrationUsersService;

    @Value("${cookie.name:authToken}")
    private String cookieName;
    @Value("${cookie.expiration:86400000}") // 24 часа по умолчанию
    private long cookieExpiration;
    @Value("${cookie.secure:false}")
    private Boolean secure;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest,
                                                HttpServletResponse response) {
        // Аутентифицируем пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Устанавливаем аутентификацию в контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерируем JWT токен
        String jwt = jwtTokenProvider.createToken(authentication);

        // Получаем информацию о пользователе
        ReadUserDTO readUserDTO = userService.getUserByEmail(loginRequest.getEmail());

        // Формируем ответ
        JwtResponseDTO auth = JwtResponseDTO.builder()
                .ok(true)
                .email(readUserDTO.getEmail())
                .build();

        // Create secure cookie
        ResponseCookie cookie = ResponseCookie.from(cookieName, jwt)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(cookieExpiration)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(auth);
    }

    @GetMapping("/me")
    public ResponseEntity<ReadUserDTO> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        ReadUserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<ReadUserDTO> register(@Valid @RequestBody CreateUserDTO createUserDTO) {
        ReadUserDTO registeredUser = registrationUsersService.register(createUserDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<ReadUserDTO> confirm(@RequestParam String token) {
        ReadUserDTO confirmedUser = registrationUsersService.confirm(token);
        return new ResponseEntity<>(confirmedUser, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
} 
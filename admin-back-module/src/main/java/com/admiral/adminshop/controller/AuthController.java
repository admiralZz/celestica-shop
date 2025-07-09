package com.admiral.adminshop.controller;

import com.admiral.adminshop.dto.LoginRequestDto;
import com.admiral.adminshop.dto.ReadUserDTO;
import com.admiral.adminshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        request.getSession(true); // создаёт JSESSIONID, если нужно

        // сохраняем в сессию вручную(это аналог работы .formLogin, но у нас его нету) иначе не будет работать
        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        contextRepository.saveContext(context, request, response);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                authentication.getPrincipal();
        log.info("[{}]: Logged in as {}", request.getRemoteAddr(), user.getUsername());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        ReadUserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Инвалидация текущей HTTP-сессии (удаляет SecurityContext и JSESSIONID)
        request.getSession(false).invalidate();

        // Очистка SecurityContext
        SecurityContextHolder.clearContext();

        // Удаляем cookie JSESSIONID с клиента
        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build(); // 204 No Content
    }
} 
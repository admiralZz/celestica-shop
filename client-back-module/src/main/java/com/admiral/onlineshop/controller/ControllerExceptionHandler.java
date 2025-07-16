package com.admiral.onlineshop.controller;

import com.admiral.common.exception.InsufficientStockException;
import com.admiral.common.exception.ProductNotFoundException;
import com.admiral.common.exception.UserAccessDeniedException;
import com.admiral.common.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.admiral.onlineshop.controller")
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Validation failed: \n\n" + Optional.of(ex.getBindingResult())
                        .map(BindingResult::getAllErrors)
                        .stream()
                        .flatMap(List::stream)
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining("\n")));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound(ProductNotFoundException ex) {
        log.warn("Product not found exception: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Product not found: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return unauthorized("User not found exception", ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleLoginProblem(BadCredentialsException ex) {
        return unauthorized("Bad credentials exception", ex);
    }

    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<?> handleLoginProblem(UserAccessDeniedException ex) {
        return unauthorized("User access denied", ex);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleLoginProblem(InternalAuthenticationServiceException ex) {
        return unauthorized("Authentication failed", ex);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException ex) {
        log.warn("Insufficient stock: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Insufficient stock for product: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unhandled exception: " + ex.getMessage());
    }

    private ResponseEntity<?> unauthorized(String title, Exception ex) {
        log.warn("{}: {}", title, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(title + ": " + ex.getMessage());
    }
}

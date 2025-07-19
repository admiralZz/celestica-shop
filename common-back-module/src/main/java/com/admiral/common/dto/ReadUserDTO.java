package com.admiral.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ReadUserDTO {

    Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @NotNull
    Instant createdAt;

} 
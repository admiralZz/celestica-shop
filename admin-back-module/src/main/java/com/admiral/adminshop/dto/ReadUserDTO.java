package com.admiral.adminshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadUserDTO {

    Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;
} 
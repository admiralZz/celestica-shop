package com.admiral.onlineshop.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtResponseDTO {
    String email;
    Boolean ok;
} 
package com.admiral.common.dto.mail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreateMailSettingsDTO {
    @NotBlank
    String host;
    @NotNull
    Integer port;
    @NotBlank
    String username;
    String password;
    @NotBlank
    String protocol;
    boolean auth;
    boolean sslEnable;
}

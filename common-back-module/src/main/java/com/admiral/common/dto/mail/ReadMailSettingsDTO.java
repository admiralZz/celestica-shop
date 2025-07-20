package com.admiral.common.dto.mail;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReadMailSettingsDTO {
    String host;
    Integer port;
    String username;
    String password;
    String protocol;
    boolean auth;
    boolean sslEnable;
}

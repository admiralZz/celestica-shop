package com.admiral.adminshop.config.init;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.init.default-admin")
public record DefaultUserProperties(
        String email,
        String password
) {
}

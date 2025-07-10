package com.admiral.common.conf.image;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.image.products")
public record ProductImageConfigProperties(
        String bucket,
        String prefixFileName
) {
}

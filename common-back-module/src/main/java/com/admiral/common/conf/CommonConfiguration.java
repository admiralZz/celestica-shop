package com.admiral.common.conf;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConfigurationPropertiesScan
@ComponentScan("com.admiral.common")
@EnableJpaRepositories(basePackages = "com.admiral.common.database.repository")
@EntityScan(basePackages = "com.admiral.common.database.model")
public class CommonConfiguration {
}

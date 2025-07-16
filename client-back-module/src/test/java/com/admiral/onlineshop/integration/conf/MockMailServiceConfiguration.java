package com.admiral.onlineshop.integration.conf;

import com.admiral.common.service.MailService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockMailServiceConfiguration {

    @Bean
    public MailService mailService() {
        return Mockito.mock(MailService.class);
    }
}

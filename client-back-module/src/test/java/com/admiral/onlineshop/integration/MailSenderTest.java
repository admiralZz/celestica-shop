package com.admiral.onlineshop.integration;

import com.admiral.common.service.mail.MailService;
import com.admiral.onlineshop.integration.conf.MockMailServiceConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;

@Import(MockMailServiceConfiguration.class)
@Slf4j
@RequiredArgsConstructor
public class MailSenderTest extends IntegrationTest {
    private final MailService mailService;

    @Test
    public void sendMail() {
        mailService.sendEmail(
                "sales@celestica-shop.com",
                "dmitriybulkin01@gmail.com",
                "Test",
                "Test mail");
        Mockito.verify(mailService, Mockito.atLeastOnce())
                .sendEmail(Mockito.anyString(), Mockito.eq("dmitriybulkin01@gmail.com"), Mockito.anyString(), Mockito.anyString());
    }
}

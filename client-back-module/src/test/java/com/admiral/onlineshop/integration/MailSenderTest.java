package com.admiral.onlineshop.integration;

import com.admiral.onlineshop.service.mail.ClientMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

@Slf4j
@Ignore
@RequiredArgsConstructor
public class MailSenderTest extends IntegrationTest {
    private final ClientMailService clientMailService;

    @Test
    public void sendMail() {
        clientMailService.sendEmail(
                "sales@celestica-shop.com",
                "dmitriybulkin01@gmail.com",
                "Test",
                "Test mail");
    }
}

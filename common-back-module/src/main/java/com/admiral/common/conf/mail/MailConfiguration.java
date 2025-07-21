package com.admiral.common.conf.mail;

import com.admiral.common.database.model.settings.MailSettings;
import com.admiral.common.dto.mail.CreateMailSettingsDTO;
import com.admiral.common.service.encrypt.MailPassowrdEncryptor;
import com.admiral.common.service.mail.MailSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailConfiguration {
    private final MailSettingsService mailSettingsService;
    private final MailPassowrdEncryptor encryptor;

    @Bean
    public JavaMailSender javaMailSender() {
        MailSettings settings = mailSettingsService.getSettingsForInnerUse()
                .orElseGet(() -> {
                    CreateMailSettingsDTO createSettings = CreateMailSettingsDTO.builder()
                            .host("mail.gmail.com")
                            .port(465)
                            .username("user@gmail.com")
                            .password("123")
                            .protocol("smtps")
                            .auth(true)
                            .sslEnable(true)
                            .build();
                    log.info("Default mail settings are creating");
                    return mailSettingsService.setSettingsForInnerUse(createSettings);
                });

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(encryptor.decrypt(settings.getPassword()));
        mailSender.setProtocol(settings.getProtocol());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", settings.isAuth());
        props.put("mail.smtp.ssl.enable", settings.isSslEnable());

        return mailSender;
    }
}

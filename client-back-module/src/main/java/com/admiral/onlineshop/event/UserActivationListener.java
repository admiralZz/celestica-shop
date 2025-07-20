package com.admiral.onlineshop.event;

import com.admiral.common.database.model.UserActivationToken;
import com.admiral.common.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActivationListener {

    @Value("${app.user.registration.activation-url:www.celestica-shop.com}")
    private String activationUrl;
    @Value("${app.user.registration.mail.from:sales@celestica-shop.com}")
    private String fromMail;
    @Value("${app.user.registration.mail.subject:Confirming registration}")
    private String subject;
    @Value("${app.user.registration.mail.body:Your link for completing registration}")
    private String body;

    private final MailService mailService;

    @Async
    @EventListener
    public void handleActivationEvent(UserActivationEvent event) {
        log.debug("Async listener running in thread: {}", Thread.currentThread().getName());
        mailService.sendEmail(fromMail,
                event.getToken().getUser().getEmail(),
                subject, createMailBody(event.getToken(), activationUrl));
    }

    private String createMailBody(UserActivationToken token, String appUrl) {
        return body + "\n" + createConfirmationLink(token.getToken(), appUrl);
    }

    private String createConfirmationLink(String token, String appUrl) {
        if (appUrl == null) {
            throw new IllegalArgumentException("Application URL must not be null");
        }

        return UriComponentsBuilder
                .fromUriString(appUrl)
                .queryParam("token", token)
                .build(true) // для защиты от XSS
                .toUriString();
    }
}

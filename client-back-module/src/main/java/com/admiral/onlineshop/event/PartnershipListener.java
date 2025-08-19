package com.admiral.onlineshop.event;

import com.admiral.common.dto.partnership.ReadPartnershipDTO;
import com.admiral.common.event.PartnershipRequestCreatedEvent;
import com.admiral.common.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PartnershipListener {

    @Value("${app.partnership.mail.from:sales@celestica-shop.com}")
    private String fromMail;
    @Value("${app.partnership.mail.to:procelestica@gmail.com}")
    private String toMail;
    @Value("${app.partnership.mail.subject:Partnership}")
    private String subject;
    @Value("${app.partnership.mail.body:Someone wants to be a partner}")
    private String body;

    private final MailService mailService;

    @Async
    @EventListener
    public void handleActivationEvent(PartnershipRequestCreatedEvent event) {
        log.debug("Async listener running in thread: {}", Thread.currentThread().getName());
        mailService.sendEmail(fromMail, toMail, subject, createFullBody(event));
    }
    
    private String createFullBody(PartnershipRequestCreatedEvent event) {
        String content = Optional.ofNullable(event.partnershipDTO())
                .map(ReadPartnershipDTO::toString).orElse("<empty>");
        return body + "\n" + content;
    }
}

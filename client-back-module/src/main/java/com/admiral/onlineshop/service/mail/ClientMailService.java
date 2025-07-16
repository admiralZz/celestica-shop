package com.admiral.onlineshop.service.mail;

import com.admiral.common.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientMailService implements MailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String from, String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // true — если тело письма в HTML
            mailSender.send(message);
            log.info("Message has been sent to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка при отправке письма", e);
        }
    }
}

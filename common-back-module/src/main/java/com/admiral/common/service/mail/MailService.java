package com.admiral.common.service.mail;

public interface MailService {

    void sendEmail(String from, String to, String subject, String body);
}

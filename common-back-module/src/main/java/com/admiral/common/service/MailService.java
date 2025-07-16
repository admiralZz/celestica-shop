package com.admiral.common.service;

public interface MailService {

    void sendEmail(String from, String to, String subject, String body);
}

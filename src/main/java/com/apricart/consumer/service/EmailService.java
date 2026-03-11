package com.apricart.consumer.service;

import javax.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

    void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException;

    void sendHtmlMessage(String[] to, String subject, String htmlBody) throws MessagingException;
}

package com.heang.drms_api.auth.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service

public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String sender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String recipient, String msgBody, String subject) {
        if (!StringUtils.hasText(sender)) {
            throw new IllegalStateException("Email sender (spring.mail.username) is not configured");
        }
        try {
            // Create a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setText(msgBody);
            mailMessage.setSubject(subject);
            // Sending the mail
            mailSender.send(mailMessage);
        } catch (Exception e) {
            // Throw a clearer runtime exception so the cause is visible in logs
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }


    }
}

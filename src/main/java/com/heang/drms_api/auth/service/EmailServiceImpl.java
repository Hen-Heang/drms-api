package com.heang.drms_api.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void EmailServiceImpleV1() {
    }

    @Override public void sendSimpleMail(String recipient, String msgBody, String subject) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setText(msgBody);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void sendOtpEmail(String recipient, String otp, String subject) {
        try {
            String body = "Your OTP code is: " + otp;
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        } catch (Exception ignored) {
        }
    }
}

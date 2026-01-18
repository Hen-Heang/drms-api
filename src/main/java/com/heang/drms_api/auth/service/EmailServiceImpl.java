package com.heang.drms_api.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailServiceImpl implements EmailService {

    @Autowired
    private  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private final String sender = "";

    public EmailServiceImpl(){
    }


    @Override
    public void sendOtpEmail(String recipient, String msgBody, String subject) {
        try {
//            Create A Simple Mail Message
            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient);
            mailMessage.setText(msgBody);
            mailMessage.setSubject(subject);
//            Sending the mail
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}

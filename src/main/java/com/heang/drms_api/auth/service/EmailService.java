package com.heang.drms_api.auth.service;

public interface EmailService {
    void sendOtpEmail(String recipient, String msgBody, String subject);
}

package com.heang.drms_api.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface OtpService {

    String generateOtp(String email);
}

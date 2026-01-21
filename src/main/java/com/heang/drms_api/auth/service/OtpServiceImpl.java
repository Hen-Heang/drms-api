package com.heang.drms_api.auth.service;


import com.heang.drms_api.auth.mapper.AuthUserMapper;
import com.heang.drms_api.auth.mapper.OtpMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.Otp;
import com.heang.drms_api.common.api.Code;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;
    private final OtpMapper otpMapper;
    private final AuthUserMapper authUserMapper;
    private final static long OTP_EXPIRATION_MINUTES = 3 * 60 * 1000; // 3 minutes in milliseconds
    private final SecureRandom secureRandom = new SecureRandom();


// Get Partner and Merchant By Email
    AppUser getPartnerByEmail (String email) {
        return otpMapper.getPartnerByEmail(email);
    }
    AppUser getMerchantByEmail (String email) {
        return otpMapper.getMerchantByEmail(email);
    }




    @Override
    public String generateOtp(String email) {
//        Step for generate OTP code
//      which logic will validate email is exist or not will be handle in controller layer
//        Verify email is exist in the system
//        Before generate OTP code we need to verify email is exist in the system or not

//        Randomly generate OTP code
        Random rand = new Random();
        int otpNumber = rand.nextInt(9000) + 1000;

//        Find user by email does it exist or not
        AppUser appUser = getPartnerByEmail(email);
        if (appUser == null) {
            appUser = getMerchantByEmail(email);
        }
        if (appUser == null) {
            throw new BadRequestException(Code.EMAIL_NOT_FOUND.getMessage());
        }

        Integer userId = appUser.getId();
//        Get current time
        long currentTimeMillis = System.currentTimeMillis();
        Otp otp;

//        Process Of Email
        emailService.sendOtpEmail(email, "Your OTP Code", "Your OTP code is: " + otpNumber + ". It will expire in 3 minutes.");
        java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTimeMillis);
        if (appUser.getRoleId() == 1){
            otp = otpMapper.generateOtpPartner(userId, otpNumber, email, timestamp);
        } else {
            otp = otpMapper.generateOtpMerchant(userId, otpNumber, email, timestamp);
        }
        if (otp == null) {
            throw new BadRequestException("Failed to generate OTP code.");
        }

        return "We've already sent you the code to " + email;
    }

    @Override
    public String verifyOtp(Integer otp, String email) {
        return "";
    }
}

package com.heang.drms_api.auth.service;


import com.heang.drms_api.auth.mapper.OtpMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.Otp;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor

public class OtpServiceImpl implements OtpService {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;

    private final OtpMapper otpMapper;
    private final EmailService emailService;


    public Boolean lessThan3MinutesCheck(Date createdDate) {
        Date currentDate = new Date();
        long diffInMillis = Math.abs(currentDate.getTime() - createdDate.getTime());
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInMinutes < 3;
    }


    Boolean checkIfUserIsActivated(String email) {
        AppUser appUser = otpMapper.checkIfActivatedByDistributorEmail(email);
        if (appUser == null) {
            appUser = otpMapper.checkIfActivatedByRetailerEmail(email);
        }
        Boolean isVerified = true;
        if (appUser != null) {
            return isVerified;
        } else {
            return false;
        }
    }

    AppUser getUserDistributorByEmail(String email) {
        return otpMapper.getUserDistributorByEmail(email);
    }

    AppUser getUserRetailerByEmail(String email) {
        return otpMapper.getUserRetailerByEmail(email);
    }

    @Override
    public String generateOtp(String email) {
        Random rand = new Random();
        Integer otpNumber = rand.nextInt(9000) +1000;

        AppUser appUser = getUserDistributorByEmail(email);
        if (appUser == null) appUser = getUserRetailerByEmail(email);
        if (appUser == null) throw new BadRequestException("This user does not exist.");

        Integer currentUserId = appUser.getId();
        long timeInMilli = System.currentTimeMillis();
        emailService.sendSimpleMail(email, "Here is your verification code: " + otpNumber,
                otpNumber + " - Warehouse master verification code");

        Timestamp time = new Timestamp(timeInMilli);
        Otp otp = (appUser.getRoleId() ==1)
                ? otpMapper.generateDistributorOtp(currentUserId, otpNumber, email, time)
                : otpMapper.generateRetailerOtp(currentUserId, otpNumber, email, time);

        if (otp == null) throw new BadRequestException("Generating OTP failed");

        return "OTP " + otpNumber + " has been sent to " + email;

    }

    @Override
    public String verifyOtp(Integer otp, String email) {
        // Check if user is already verified
        if (checkIfUserIsActivated(email)) {
            throw new BadRequestException("This User is already verified");
        }
        // get user by email
        AppUser appUser = new AppUser();
        Otp otpObj = new Otp();
        appUser = getUserDistributorByEmail(email);
        otpObj = otpMapper.getDistributorOtpByEmail(email);
        if (appUser == null || otpObj == null) {
            appUser = getUserRetailerByEmail(email);
            otpObj = otpMapper.getRetailerOtpByEmail(email);
        }

        String returnMsg = "User has been verified";
        if (appUser == null) {
            throw new BadRequestException("This user does not exist.");
        }
        if (otpObj == null) {
            throw new BadRequestException("This OTP code does not exist.");
        }
        // check if request and database of OTP matches
        if (!Objects.equals(appUser.getEmail(), otpObj.getEmail())) {
            throw new BadRequestException("Email not match");
        } else if (!Objects.equals(otpObj.getOtpCode(), otp)) {
            throw new BadRequestException("OTP code not match");
        }
        // check timeout of 3 minutes
        else if (!lessThan3MinutesCheck(otpObj.getCreatedDate())) {
            throw new BadRequestException("OTP Expired");
        }
        // if everything is success, verify that email

        String confirm = otpMapper.verifyDistributor(email);
        if (Objects.equals(confirm, "1")) {
            return returnMsg;
        }

        confirm = otpMapper.verifyRetailer(email);
        if (Objects.equals(confirm, "1")) {
            return returnMsg;
        }
        return "Verifying OTP failed";
    }
}

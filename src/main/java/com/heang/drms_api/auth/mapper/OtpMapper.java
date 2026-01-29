package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.Otp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface OtpMapper {

    // ======================
    // ACTIVATION CHECK
    // ======================
    AppUser checkIfActivatedByPartnerEmail(@Param("email") String email);
    AppUser checkIfActivatedByMerchantEmail(@Param("email") String email);

    // ======================
    // GENERATE OTP
    // ======================
    Otp generatePartnerOtp(
            @Param("currentUserId") Integer currentUserId,
            @Param("otpNumber") Integer otpNumber,
            @Param("email") String email,
            @Param("time") Timestamp time
    );

    Otp generateMerchantOtp(
            @Param("currentUserId") Integer currentUserId,
            @Param("otpNumber") Integer otpNumber,
            @Param("email") String email,
            @Param("time") Timestamp time
    );

    // ======================
    // GET USER
    // ======================
    AppUser getUserPartnerByEmail(@Param("email") String email);
    AppUser getUserMerchantByEmail(@Param("email") String email);

    // ======================
    // GET OTP
    // ======================
    Otp getPartnerOtpByEmail(@Param("email") String email);
    Otp getMerchantOtpByEmail(@Param("email") String email);

    // ======================
    // VERIFY EMAIL
    // ======================
    String verifyPartner(@Param("email") String email);
    String verifyMerchant(@Param("email") String email);
}

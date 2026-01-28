package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.Otp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;


@Mapper
public interface OtpMapper {

    AppUser checkIfActivatedByDistributorEmail(@Param("email") String email);
    AppUser checkIfActivatedByRetailerEmail(@Param("email") String email);

    Otp generateDistributorOtp(
            @Param("currentUserId") Integer currentUserId,
            @Param("otpNumber") Integer otpNumber,
            @Param("email") String email,
            @Param("time") Timestamp time
    );

    Otp generateRetailerOtp(
            @Param("currentUserId") Integer currentUserId,
            @Param("otpNumber") Integer otpNumber,
            @Param("email") String email,
            @Param("time") Timestamp time
    );

    AppUser getUserDistributorByEmail(@Param("email") String email);
    AppUser getUserRetailerByEmail(@Param("email") String email);

    Otp getDistributorOtpByEmail(@Param("email") String email);
    Otp getRetailerOtpByEmail(@Param("email") String email);

    String verifyDistributor(@Param("email") String email);
    String verifyRetailer(@Param("email") String email);
}

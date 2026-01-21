package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.Otp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OtpMapper {

    AppUser getPartnerByEmail(String email);
    AppUser getMerchantByEmail(String email);
    Otp generateOtpPartner(@Param("currentUserId") Integer currentUserId,
                           @Param("otpNumber") Integer otpNumber,
                           @Param("email") String email,
                           @Param("time") java.sql.Timestamp time);
    Otp generateOtpMerchant(@Param("currentUserId") Integer currentUserId,
                            @Param("otpNumber") Integer otpNumber,
                            @Param("email") String email,
                            @Param("time") java.sql.Timestamp time);
}

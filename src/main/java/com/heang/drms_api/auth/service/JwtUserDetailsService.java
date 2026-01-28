package com.heang.drms_api.auth.service;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import org.apache.ibatis.javassist.NotFoundException;

public interface JwtUserDetailsService {
    AppUserDto insertUser(AppUserRequest appUserRequest);

    boolean getVerifyEmail(String email);


    AppUserDto changePassword(JwtChangePasswordRequest request) throws NotFoundException;

    String forgetPassword(Integer otp, String email, String newPassword) throws NotFoundException;
}

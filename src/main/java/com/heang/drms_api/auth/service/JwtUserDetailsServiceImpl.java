package com.heang.drms_api.auth.service;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.mapper.AuthUserMapper;
import com.heang.drms_api.auth.mapper.OtpMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import com.heang.drms_api.auth.model.Otp;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NullMarked
@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService, JwtUserDetailsService {

    private final AuthUserMapper authUserMapper;
    private final OtpMapper otpMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    Boolean checkDuplicatePhone(String phone, Integer roleId) {
        boolean isExistInUserPhone;
        boolean isExistInUserInfo;

        // roleId == 1 => Partner, roleId == 2 => Merchant
        if (roleId == 1) {
            isExistInUserPhone = authUserMapper.checkPhoneNumberFromPartnerPhone(phone);
            isExistInUserInfo = authUserMapper.checkPhoneNumberFromPartnerDetail(phone);
        } else {
            isExistInUserPhone = authUserMapper.checkPhoneNumberFromMerchantPhone(phone);
            isExistInUserInfo = authUserMapper.checkPhoneNumberFromMerchantDetail(phone);
        }
        return isExistInUserPhone || isExistInUserInfo;
    }

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validateEmail(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = authUserMapper.findPartnerUserByEmail(email);
        if (user == null) {
            user = authUserMapper.findMerchantUserByEmail(email);
        }
        if (user == null) {
            throw new BadRequestException("Invalid email address. Please input valid email address.");
        }
        return user;
    }

    @Override
    public AppUserDto insertUser(AppUserRequest appUserRequest) {
        if (!(appUserRequest.getRoleId().equals(1) || appUserRequest.getRoleId().equals(2))) {
            throw new BadRequestException("Invalid roleId.");
        }
        if (appUserRequest.getEmail().isBlank()) {
            throw new BadRequestException("Email can not be null");
        }
        if (!validateEmail(appUserRequest.getEmail())) {
            throw new BadRequestException("Please follow email format.");
        }
        if (appUserRequest.getPassword().isBlank()) {
            throw new BadRequestException("Password can not be null");
        }

        // encode password
        appUserRequest.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        // duplicate email check (Partner + Merchant)
        AppUser dupPartner = authUserMapper.findPartnerUserByEmail(appUserRequest.getEmail());
        AppUser dupMerchant = authUserMapper.findMerchantUserByEmail(appUserRequest.getEmail());
        if (dupPartner != null || dupMerchant != null) {
            throw new BadRequestException("Email is already in use.");
        }

        AppUser appUser = null;

        // roleId == 1 => Partner
        if (appUserRequest.getRoleId() == 1) {
            if (appUserRequest.getPassword().equals("string") || appUserRequest.getPassword().isBlank()) {
                throw new BadRequestException("Invalid password");
            }
            appUser = authUserMapper.insertPartnerUser(appUserRequest);

            // roleId == 2 => Merchant
        } else if (appUserRequest.getRoleId() == 2) {
            if (appUserRequest.getPassword().equals("string") || appUserRequest.getPassword().isBlank()) {
                throw new BadRequestException("Invalid password");
            }
            appUser = authUserMapper.insertMerchantUser(appUserRequest);
        }

        return modelMapper.map(appUser, AppUserDto.class);
    }

    @Override
    public boolean getVerifyEmail(String email) {
        // if you want “auto-detect” (partner/merchant), tell me.
        return authUserMapper.getVerifyPartnerEmail(email);
    }

    @Override
    public AppUserDto changePassword(JwtChangePasswordRequest request) throws NotFoundException {
        boolean isPartner = true;

        AppUser appUser = authUserMapper.findPartnerUserByEmail(request.getEmail());
        if (appUser == null) {
            isPartner = false;
            appUser = authUserMapper.findMerchantUserByEmail(request.getEmail());
        }
        if (appUser == null) {
            throw new NotFoundException("Not found. Invalid email.");
        }

        // verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), appUser.getPassword())) {
            throw new NotFoundException("Old password is incorrect. Please input correct password.");
        }

        // encode new password + update
        request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));

        AppUser newAppUser;
        if (isPartner) {
            newAppUser = authUserMapper.updatePartnerUser(request);
        } else {
            newAppUser = authUserMapper.updateMerchantUser(request);
        }

        return modelMapper.map(newAppUser, AppUserDto.class);
    }

    @Override
    public String forgetPassword(Integer otp, String email, String newPassword) throws NotFoundException {
        boolean isPartner = true;

        AppUser appUser = authUserMapper.findPartnerUserByEmail(email);
        Otp otpObj = otpMapper.getPartnerOtpByEmail(email);

        if (appUser == null || otpObj == null) {
            isPartner = false;
            appUser = authUserMapper.findMerchantUserByEmail(email);
            otpObj = otpMapper.getMerchantOtpByEmail(email);
        }

        if (appUser == null) {
            throw new NotFoundException("Not found. Invalid email.");
        }
        if (otpObj == null) {
            throw new BadRequestException("This OTP does not exist");
        }

        if (!Objects.equals(appUser.getEmail(), otpObj.getEmail())) {
            throw new BadRequestException("Email not match");
        } else if (!Objects.equals(otpObj.getOtpCode(), otp)) {
            throw new BadRequestException("OTP code not match");
        } else if (!lessThan3MinutesCheck(otpObj.getCreatedDate())) {
            throw new BadRequestException("OTP Expired");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        String updatedPassword;
        if (isPartner) {
            updatedPassword = authUserMapper.updateForgetPartnerUser(email, encodedNewPassword);
        } else {
            updatedPassword = authUserMapper.updateForgetMerchantUser(email, encodedNewPassword);
        }

        if (Objects.equals(updatedPassword, "null")) {
            throw new BadRequestException("Failed to update new password");
        }

        return "New password updated. Your new password is: " + newPassword;
    }

    public Integer getRoleIdByMail(String email) {
        // if you want to auto-detect partner/merchant, tell me.
        return authUserMapper.getRoleIdByMailPartner(email);
    }

    public Boolean lessThan3MinutesCheck(Date createdDate) {
        Date currentDate = new Date();
        long diffInMillis = Math.abs(currentDate.getTime() - createdDate.getTime());
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInMinutes < 3;
    }

    public Integer getUserIdByMail(String email) {
        // if you want to auto-detect partner/merchant, tell me.
        return authUserMapper.getUserIdByMailPartner(email);
    }
}

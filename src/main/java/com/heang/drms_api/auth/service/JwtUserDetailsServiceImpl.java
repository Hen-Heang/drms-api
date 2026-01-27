package com.heang.drms_api.auth.service;


import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.mapper.AppUserStructMapper;
import com.heang.drms_api.auth.mapper.AuthUserMapper;
import com.heang.drms_api.auth.mapper.OtpMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.common.api.Code;
import com.heang.drms_api.common.utils.EmailValidatorUtils;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService, AuthService {

    private final AuthUserMapper authUserMapper;
    private final AppUserStructMapper appUserStructMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpMapper otpMapper;


    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserDetails userDetails = authUserMapper.findPartnerByEmail(username);
        if (userDetails == null) {
            userDetails = authUserMapper.findMerchantByEmail(username);
        }
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return userDetails;
    }

    @Override
    public AppUserDto insertUser(AppUserRequest appUserRequest) {
        //        check roleId
        if ((!appUserRequest.getRoleId().equals(1) && !appUserRequest.getRoleId().equals(2))) {
            throw new IllegalArgumentException(Code.INVALID_ROLE_ID.getMessage());
        }

        // check can't be null or blank email
        if (appUserRequest.getEmail() == null || appUserRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException(Code.EMAIL_CANNOT_BE_NULL.getMessage());
        }
//        if (EmailValidatorUtils.isValid(appUserRequest.getEmail())) {
//            throw new IllegalArgumentException(Code.INVALID_EMAIL_FORMAT.getMessage());
//        }
        if (!EmailValidatorUtils.isValid(appUserRequest.getEmail())) {
            throw new BadRequestException(Code.INVALID_EMAIL_FORMAT.getMessage());
        }
        if (appUserRequest.getPassword() == null || appUserRequest.getPassword().isBlank()) {
            throw new BadRequestException(Code.PASSWORD_CANNOT_BE_NULL.getMessage());
        }
        if ("string".equals(appUserRequest.getPassword())) {
            throw new BadRequestException(Code.INVALID_PASSWORD.getMessage());
        }

        //  check duplicate email in both tables
        AppUser dupPartner = authUserMapper.findPartnerByEmail(appUserRequest.getEmail());
        AppUser dupMerchant = authUserMapper.findMerchantByEmail(appUserRequest.getEmail());
        if (dupPartner != null || dupMerchant != null) {
            throw new BadRequestException(Code.EMAIL_ALREADY_EXISTS.getMessage());
        }
        appUserRequest.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        AppUser saved = null;
        if (appUserRequest.getRoleId() == 1) {
            saved = authUserMapper.insertPartnerUser(appUserRequest);
        } else if (appUserRequest.getRoleId() == 2) {
            saved = authUserMapper.insertMerchantUser(appUserRequest);
        }
        return appUserStructMapper.toDto(saved);
    }

    // Get RoleId by email
    public Integer getRoleIdByEmail(String email) {
        Integer roleId = authUserMapper.getPartnerRoleIdByEmail(email);
        if (roleId == null) {
            roleId = authUserMapper.getMerchantRoleIdByEmail(email);
        }
        return roleId;

    }

    // Find User I'd By Email
    public Integer findUserIdByEmail(String email) {
        AppUser user = authUserMapper.findPartnerByEmail(email);
        if (user == null) {
            user = authUserMapper.findMerchantByEmail(email);
        }
        if (user == null) {
            throw new BadRequestException("User not found with email: " + email);
        }
        return user.getId();
    }


    public boolean isEmailVerified(String email) {
        Boolean isVerified = authUserMapper.verifyPartnerEmail(email);
        if (isVerified == null) {
            isVerified = authUserMapper.verifyMerchantEmail(email);
        }
        if (isVerified == null) {
            throw new BadRequestException(Code.EMAIL_NOT_FOUND.getMessage());
        }
        return isVerified;
    }
}

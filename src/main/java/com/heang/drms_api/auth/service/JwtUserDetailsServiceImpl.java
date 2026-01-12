package com.heang.drms_api.auth.service;


import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.mapper.AppUserStructMapper;
import com.heang.drms_api.auth.mapper.AuthUserMapper;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService, AuthService {

    private final AuthUserMapper authUserMapper;

     private final AppUserStructMapper appUserStructMapper;
    private final  PasswordEncoder passwordEncoder;
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validateEmail(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
            throw new IllegalArgumentException("Invalid roleId.");
        }

        // check can't be null or blank email
        if (appUserRequest.getEmail() == null || appUserRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email can not be null");
        }

        if (validateEmail(appUserRequest.getEmail())) {
            throw new IllegalArgumentException("Please follow email format.");
        }
        if (validateEmail(appUserRequest.getEmail())) {
            throw new BadRequestException("Please follow email format.");
        }
        if (appUserRequest.getPassword() == null || appUserRequest.getPassword().isBlank()) {
            throw new BadRequestException("Password can not be null");
        }
        if ("string".equals(appUserRequest.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        //  check duplicate email in both tables
        AppUser dupPartner  = authUserMapper.findPartnerByEmail(appUserRequest.getEmail());
        AppUser dupMerchant = authUserMapper.findMerchantByEmail(appUserRequest.getEmail());
        if (dupPartner != null || dupMerchant != null) {
            throw new BadRequestException("This email is already taken");
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
}

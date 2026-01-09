package com.heang.drms_api.auth.service;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.dto.RegisterRequest;
import com.heang.drms_api.auth.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService {
    AppUserDto insertUser(@Valid AppUserRequest appUserRequest);

    public static interface JwtService {
        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

        RegisterResponse register(@Valid RegisterRequest registerRequest);
    }
}

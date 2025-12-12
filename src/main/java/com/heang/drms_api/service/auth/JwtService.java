package com.heang.drms_api.service.auth;

import com.heang.drms_api.dto.RegisterRequest;
import com.heang.drms_api.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public interface JwtService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    RegisterResponse register(@Valid RegisterRequest registerRequest);
}

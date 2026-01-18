package com.heang.drms_api.auth.service;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    AppUserDto insertUser(@Valid AppUserRequest appUserRequest);

}

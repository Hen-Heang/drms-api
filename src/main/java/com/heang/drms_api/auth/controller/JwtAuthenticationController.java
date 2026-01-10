package com.heang.drms_api.auth.controller;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.service.OtpService;
import com.heang.drms_api.common.api.ApiResponse;
import com.heang.drms_api.common.api.ApiStatus;
import com.heang.drms_api.common.api.Code;
import com.heang.drms_api.common.api.Common;
import com.heang.drms_api.common.utils.DateTimeUtils;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.auth.dto.RegisterRequest;
import com.heang.drms_api.auth.dto.RegisterResponse;
import com.heang.drms_api.auth.service.JwtUserDetailsServiceImpl;
import com.heang.drms_api.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "APIs for user authentication and JWT token management")

public class JwtAuthenticationController extends BaseController {

    private final JwtTokenUtil jwtTokenUtil;
    private final  JwtUserDetailsServiceImpl jwtUserDetailsService;
//    private final OtpService otpService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody AppUserRequest appUserRequest,
            @RequestHeader Map<String, String> headers
            ){
// logic to register user
        AppUserDto createUser = jwtUserDetailsService.insertUser(appUserRequest);
//        build response
//       Build common
        Common common = new Common(headers);
        return created(createUser, common);
    }


}

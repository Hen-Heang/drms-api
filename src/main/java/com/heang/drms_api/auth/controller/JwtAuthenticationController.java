package com.heang.drms_api.auth.controller;


import com.heang.drms_api.auth.dto.ApiResponse;
import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.dto.LoginResponse;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import com.heang.drms_api.auth.model.JwtRequest;
import com.heang.drms_api.auth.service.JwtUserDetailsServiceImpl;
import com.heang.drms_api.auth.service.OtpService;

import com.heang.drms_api.exception.BadRequestException;
import com.heang.drms_api.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/authorization")
@Tag(name = "API authorization")
public class JwtAuthenticationController {
    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;
    private final OtpService otpService;

    public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, JwtUserDetailsServiceImpl jwtUserDetailsService, OtpService otpService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> insertUser(@RequestBody AppUserRequest appUserRequest){
        AppUserDto appUserDto = jwtUserDetailsService.insertUser(appUserRequest);
        ApiResponse<AppUserDto> response = ApiResponse.<AppUserDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("successfully register new user")
                .data(appUserDto)
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        if(!(verifyEmail(authenticationRequest.getEmail()))){
            otpService.generateOtp(authenticationRequest.getEmail());
            throw new BadRequestException("Email is not verified. We just sent you a verification code.");
        }
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        Integer roleId = jwtUserDetailsService.getRoleIdByMail(authenticationRequest.getEmail());
        Integer userId = jwtUserDetailsService.getUserIdByMail(authenticationRequest.getEmail());
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login success.")
                .data(new LoginResponse(token, roleId, userId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }
    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("INVALID_PASSWORD. Please input correct password.");
        }
    }
    public boolean verifyEmail(String email){
        return jwtUserDetailsService.getVerifyEmail(email);
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody JwtChangePasswordRequest request) throws NotFoundException {
        ApiResponse<AppUserDto> response = ApiResponse.<AppUserDto>builder()
                .status(HttpStatus.OK.value())
                .message("Change password successfully")
                .data(jwtUserDetailsService.changePassword(request))
                .date(formatter.format(new Date()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestParam Integer otp, @RequestParam String email, @RequestParam String newPassword) throws NotFoundException {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Set new password successfully")
                .data(jwtUserDetailsService.forgetPassword(otp, email, newPassword))
                .date(formatter.format(new Date()))
                .build();
        return ResponseEntity.ok(response);
    }
}

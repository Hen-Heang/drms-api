package com.heang.drms_api.auth.controller;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.dto.LoginResponse;
import com.heang.drms_api.auth.model.JwtRequest;
import com.heang.drms_api.common.api.Common;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.auth.service.JwtUserDetailsServiceImpl;
import com.heang.drms_api.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody AppUserRequest appUserRequest,
            @RequestHeader Map<String, String> headers
            ){
        AppUserDto createUser = jwtUserDetailsService.insertUser(appUserRequest);
        Common common = new Common(headers);
        return created(createUser, common);
    }

    @PostMapping("/login")
    public  ResponseEntity<?> createAuthenticationToken(
            JwtRequest loginRequest) throws Exception {

//        Apply authentication logic
        authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//       Generate JWT token upon successful authentication
        final var userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
//        Get user details
        Integer roleId = jwtUserDetailsService.getRoleIdByEmail(loginRequest.getEmail());
        Integer userId = jwtUserDetailsService.findUserIdByEmail(loginRequest.getEmail());
        return ok(new LoginResponse(token, roleId, userId));

    }


//    Authentication logic to be implemented
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}

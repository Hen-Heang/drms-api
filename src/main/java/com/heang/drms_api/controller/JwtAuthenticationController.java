package com.heang.drms_api.controller;

import com.heang.drms_api.common.api.ApiResponse;
import com.heang.drms_api.common.api.Common;
import com.heang.drms_api.dto.RegisterRequest;
import com.heang.drms_api.dto.RegisterResponse;
import com.heang.drms_api.service.auth.JwtUserDetailsServiceImpl;
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

public class JwtAuthenticationController extends  BaseController {

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;

//    Register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestHeader Map<String, String> headers,
          @Valid @RequestBody  RegisterRequest registerRequest){

        Common common = new Common(headers);
        RegisterResponse response = jwtUserDetailsService.register(registerRequest);
                return ok(response, common);

    }


}

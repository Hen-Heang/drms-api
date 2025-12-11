package com.heang.drms_api.controller;

import com.heang.drms_api.service.auth.JwtUserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "APIs for user authentication and JWT token management")

public class JwtAuthenticationController {

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;

//    Register



}

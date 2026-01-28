package com.heang.drms_api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        // Create the response body
//        ObjectMapper objectMapper = new ObjectMapper();
//        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
//                HttpStatus.UNAUTHORIZED,
//                "Unauthorized access",
//                authException.getMessage()
//        );
//        String responseBody = objectMapper.writeValueAsString(apiErrorResponse);
//
//        // Write the response body to the output stream
//        PrintWriter writer = response.getWriter();
//        writer.write(responseBody);
//        writer.flush();
//    }
}

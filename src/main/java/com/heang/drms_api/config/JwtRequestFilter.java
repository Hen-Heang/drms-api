package com.heang.drms_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heang.drms_api.service.auth.JwtUserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsServiceImpl jwtUserDetailsService;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetailsServiceImpl jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


//  1. Skip some paths (login, register, etc.)
        String path = request.getServletPath();
        if (path.startsWith("/auth") || path.equals("/login")) {
            filterChain.doFilter(request, response);
        }

//  2. Read Authentication Header
    String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username;
        try {
            // 4️⃣ Extract username from token (subject field)
            username = jwtTokenUtil.getUsernameFromToken(token);
        }
        // Token is expired
        catch (ExpiredJwtException e) {
            sendError(response, HttpStatus.UNAUTHORIZED, "JWT token is expired");
            return;
        }
        // Token is the wrong format or invalid signature
        catch (JwtException e) {
            sendError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
            return;
        }
        // Any unknown error
        catch (Exception e) {
            sendError(response, HttpStatus.UNAUTHORIZED, "Cannot parse JWT token");
            return;
        }

// 5️⃣ Check if the user is NOT already authenticated in this request
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            // 6️⃣ Load user details from DB by username (email)
//            // This returns user + roles from your database
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
//
//            // 7️⃣ Validate token with your JwtTokenUtil
//            // Checks username match + expiration date
//            if (jwtTokenUtil.validateToken(token, userDetails)) {
//
//                // 8️⃣ Create Authentication object (Spring Security way to store logged-in user)
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails, // principal (who is logged in)
//                                null,        // no password in token-based auth
//                                userDetails.getAuthorities() // user roles/permissions
//                        );
//
//                // Attach request details (IP, session, etc.)
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//
//                // 9️⃣ Store authentication into SecurityContext → user is now "logged in"
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }

        // 🔟 Continue a filter chain (move to the next filter or controller)
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to send a JSON error response.
     */
    private void sendError(HttpServletResponse response,
                           HttpStatus status,
                           String message) throws IOException {

        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> body = new HashMap<>();
        body.put("error", message);

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

}

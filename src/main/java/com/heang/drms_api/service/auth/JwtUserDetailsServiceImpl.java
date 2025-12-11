package com.heang.drms_api.service.auth;


import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    /**
     * Minimal implementation to satisfy DaoAuthenticationProvider wiring.
     * TODO: replace with real user lookup (DB/repository) and proper authorities/password handling.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For now, throw UsernameNotFoundException to indicate user-not-found behavior.
        // Implement actual lookup and return a UserDetails instance when ready.
        throw new UsernameNotFoundException("User not found: " + username);
    }
}

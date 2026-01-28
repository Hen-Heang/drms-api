package com.heang.drms_api.security;

import com.heang.drms_api.auth.model.AppUser;
 import lombok.experimental.UtilityClass;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public static Integer currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {

                      throw new AuthenticationCredentialsNotFoundException("Unauthenticated");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof AppUser appUser) {
            return appUser.getId();
        }

        // Common setups put username here as a String.
        if (principal instanceof String) {
                     throw new AuthenticationCredentialsNotFoundException(
                                    "Authenticated principal is String; resolve userId from username instead of casting to AppUser"
                                        );
        }
        assert principal != null;
            throw new AuthenticationCredentialsNotFoundException("Unsupported principal type: " + principal.getClass().getName());
    }

}

package com.apricart.consumer.utils;

import com.apricart.consumer.security.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helpers for reading the current security context on open (permitAll) endpoints
 * that optionally receive an admin JWT.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isAdminAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (UserRole.ADMIN.name().equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}

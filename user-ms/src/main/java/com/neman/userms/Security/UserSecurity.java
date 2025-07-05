package com.neman.userms.Security;

import com.neman.userms.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserSecurity {

    public boolean isOwner(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Checking ownership for userId: {} with authentication: {}", userId, authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("Authentication is null or not authenticated");
            return false;
        }

        // Check if user has ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        log.debug("User authorities: {}", authentication.getAuthorities());
        log.debug("Is admin: {}", isAdmin);

        if (isAdmin) {
            log.debug("User is admin, allowing access");
            return true;
        }

        Object principal = authentication.getPrincipal();
        log.debug("Principal class: {}", principal.getClass().getName());

        if (!(principal instanceof User)) {
            log.debug("Principal is not instance of User");
            return false;
        }

        User user = (User) principal;
        boolean isOwner = user.getId().equals(userId);
        log.debug("Checking if user {} is owner of resource {}: {}", user.getId(), userId, isOwner);

        return isOwner;
    }
}

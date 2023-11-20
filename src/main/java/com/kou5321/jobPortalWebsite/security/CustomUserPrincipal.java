package com.kou5321.jobPortalWebsite.security;

import org.springframework.security.core.GrantedAuthority;
import com.kou5321.jobPortalWebsite.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

import java.util.UUID;

public class CustomUserPrincipal implements UserDetails {
    private User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Here you can expose any additional information from the User entity
    public UUID getId() {
        return user.getId(); // Return the UUID directly
    }

    public String getEmail() {
        return user.getEmail();
    }

    // Specify whether the user's account is expired
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic or leave as true
    }

    // Specify whether the user is locked or unlocked
    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic or leave as true
    }

    // Specify whether the user's credentials (password) are expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic or leave as true
    }

    // Specify whether the user is enabled or disabled
    @Override
    public boolean isEnabled() {
        return true; // Implement your logic or leave as true
    }

    // Get the underlying User entity
    public User getUser() {
        return user;
    }
}

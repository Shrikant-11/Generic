package com.example.userservice.security;


import com.example.userservice.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getMobileNumber() {
        return user.getMobileNumber();
    }

    public String getEducation() {
        return user.getEducation();
    }

    public String getAddress() {
        return user.getAddress();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole().toUpperCase());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // You can make this dynamic
    }

    public User getUser() {
        return user;
    }
}

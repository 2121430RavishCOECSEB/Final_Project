package com.fitness.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    
    private final String username;
    private final String password;
    private final String role;
    private final String email;
    private final String specialization;
    private final boolean enabled;
    
    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = "ROLE_USER";
        this.email = user.getEmail();
        this.specialization = null;
        this.enabled = true;
    }
    
    public CustomUserDetails(Coach coach) {
        this.username = coach.getUsername();
        this.password = coach.getPassword();
        this.role = "ROLE_COACH";
        this.email = coach.getEmail();
        this.specialization = coach.getSpecialization();
        this.enabled = coach.getIsAvailable();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getSpecialization() {
        return specialization;
    }
} 
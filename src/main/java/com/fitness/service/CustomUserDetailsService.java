package com.fitness.service;

import com.fitness.model.CustomUserDetails;
import com.fitness.model.User;
import com.fitness.model.Coach;
import com.fitness.repository.UserRepository;
import com.fitness.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CoachRepository coachRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, CoachRepository coachRepository) {
        this.userRepository = userRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username);
        
        // First try to find a user
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            System.out.println("Found user: " + username);
            return new CustomUserDetails(user);
        }
        
        // If not found as user, try to find as coach
        Coach coach = coachRepository.findByUsername(username).orElse(null);
        if (coach != null) {
            System.out.println("Found coach: " + username);
            return new CustomUserDetails(coach);
        }
        
        // If neither found, throw exception
        System.out.println("User not found: " + username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
} 
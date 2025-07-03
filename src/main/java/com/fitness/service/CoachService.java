package com.fitness.service;

import com.fitness.model.Coach;
import com.fitness.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoachService {
    
    private final CoachRepository coachRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public CoachService(CoachRepository coachRepository, PasswordEncoder passwordEncoder) {
        this.coachRepository = coachRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }
    
    public Optional<Coach> getCoachById(Long id) {
        return coachRepository.findById(id);
    }
    
    public Optional<Coach> getCoachByUsername(String username) {
        return coachRepository.findByUsername(username);
    }
    
    public Coach saveCoach(Coach coach) {
        // Encode password before saving
        if (coach.getPassword() != null && !coach.getPassword().startsWith("$2a$")) {
            coach.setPassword(passwordEncoder.encode(coach.getPassword()));
        }
        return coachRepository.save(coach);
    }
    
    public void deleteCoach(Long id) {
        coachRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return coachRepository.findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return coachRepository.findByEmail(email).isPresent();
    }
    
    public Optional<Coach> getCoachByEmail(String email) {
        return coachRepository.findByEmail(email);
    }
    
    public List<Coach> getAvailableCoaches() {
        return coachRepository.findAll().stream()
                .filter(Coach::getIsAvailable)
                .toList();
    }
} 
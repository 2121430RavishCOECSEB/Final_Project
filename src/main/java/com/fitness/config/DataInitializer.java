package com.fitness.config;

import com.fitness.model.Coach;
import com.fitness.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CoachRepository coachRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(CoachRepository coachRepository, PasswordEncoder passwordEncoder) {
        this.coachRepository = coachRepository;
        this.passwordEncoder = passwordEncoder;
    }
// hello
    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataInitializer starting...");
        System.out.println("Current coach count: " + coachRepository.count());

        // Create a default coach if no coaches exist
        if (coachRepository.count() == 0) {
            System.out.println("Creating default coach...");
            Coach defaultCoach = new Coach();
            defaultCoach.setUsername("coach");
            defaultCoach.setEmail("coach@fitness.com");
            defaultCoach.setPassword(passwordEncoder.encode("coach123"));
            defaultCoach.setFirstName("John");
            defaultCoach.setLastName("Fitness");
            defaultCoach.setAge(30);
            defaultCoach.setSpecialization("Personal Training, Strength Training, Nutrition");
            defaultCoach.setExperienceYears(5);
            defaultCoach.setCertifications("NASM-CPT, ACE-CPT, Precision Nutrition Level 1");
            defaultCoach.setBio("Experienced personal trainer with 5+ years helping clients achieve their fitness goals.");
            defaultCoach.setHourlyRate(50.0);
            defaultCoach.setIsAvailable(true);
            defaultCoach.setGender(Coach.Gender.MALE);
            
            // Save directly to repository to avoid double password encoding
            Coach savedCoach = coachRepository.save(defaultCoach);
            System.out.println("Default coach created with ID: " + savedCoach.getId());
            System.out.println("Default coach created: coach/coach123");
        } else {
            System.out.println("Coaches already exist, skipping default coach creation");
        }
        
        System.out.println("Final coach count: " + coachRepository.count());
    }
} 
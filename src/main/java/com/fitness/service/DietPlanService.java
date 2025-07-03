package com.fitness.service;

import com.fitness.model.DietPlan;
import com.fitness.model.User;
import com.fitness.repository.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietPlanService {
    private final DietPlanRepository dietPlanRepository;

    @Autowired
    public DietPlanService(DietPlanRepository dietPlanRepository) {
        this.dietPlanRepository = dietPlanRepository;
    }

    public DietPlan savePlan(DietPlan plan) {
        return dietPlanRepository.save(plan);
    }

    public List<DietPlan> getAllPlans() {
        return dietPlanRepository.findAll();
    }

    public Optional<DietPlan> getPlanById(Long id) {
        return dietPlanRepository.findById(id);
    }

    public List<DietPlan> getPlansByUser(User user) {
        return dietPlanRepository.findByAssignedUser(user);
    }

    public List<DietPlan> getPlansByUserId(Long userId) {
        return dietPlanRepository.findByAssignedUserId(userId);
    }

    public void deletePlan(Long id) {
        dietPlanRepository.deleteById(id);
    }
} 
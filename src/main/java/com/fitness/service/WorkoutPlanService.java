package com.fitness.service;

import com.fitness.model.WorkoutPlan;
import com.fitness.model.User;
import com.fitness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public WorkoutPlan savePlan(WorkoutPlan plan) {
        return workoutPlanRepository.save(plan);
    }

    public List<WorkoutPlan> getAllPlans() {
        return workoutPlanRepository.findAll();
    }

    public Optional<WorkoutPlan> getPlanById(Long id) {
        return workoutPlanRepository.findById(id);
    }

    public List<WorkoutPlan> getPlansByUser(User user) {
        return workoutPlanRepository.findByAssignedUser(user);
    }

    public List<WorkoutPlan> getPlansByUserId(Long userId) {
        return workoutPlanRepository.findByAssignedUserId(userId);
    }

    public void deletePlan(Long id) {
        workoutPlanRepository.deleteById(id);
    }

    public void deletePlanById(Long id) {
        deletePlan(id);
    }
}

package com.fitness.repository;

import com.fitness.model.WorkoutPlan;
import com.fitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByAssignedUser(User user);
    List<WorkoutPlan> findByAssignedUserId(Long userId);
} 
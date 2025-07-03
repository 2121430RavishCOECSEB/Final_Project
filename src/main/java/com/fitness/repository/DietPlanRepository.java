package com.fitness.repository;

import com.fitness.model.DietPlan;
import com.fitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByAssignedUser(User user);
    List<DietPlan> findByAssignedUserId(Long userId);
} 
package com.fitness.controller;

import com.fitness.model.WorkoutPlan;
import com.fitness.model.DietPlan;
import com.fitness.model.User;
import com.fitness.service.WorkoutPlanService;
import com.fitness.service.DietPlanService;
import com.fitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final DietPlanService dietPlanService;
    private final UserService userService;

    @Autowired
    public UserPlanController(WorkoutPlanService workoutPlanService, DietPlanService dietPlanService, UserService userService) {
        this.workoutPlanService = workoutPlanService;
        this.dietPlanService = dietPlanService;
        this.userService = userService;
    }

    // View assigned workout plans
    @GetMapping("/workout-plans")
    public String myWorkoutPlans(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElse(null);
        if (user != null) {
            List<WorkoutPlan> plans = workoutPlanService.getPlansByUser(user);
            model.addAttribute("plans", plans);
        }
        return "user/workout-plans";
    }

    // Mark workout plan as completed
    @PostMapping("/workout-plans/{id}/complete")
    public String completeWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.getPlanById(id).ifPresent(plan -> {
            plan.setCompleted(true);
            workoutPlanService.savePlan(plan);
        });
        return "redirect:/user/workout-plans";
    }

    // View assigned diet plans
    @GetMapping("/diet-plans")
    public String myDietPlans(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElse(null);
        if (user != null) {
            List<DietPlan> plans = dietPlanService.getPlansByUser(user);
            model.addAttribute("plans", plans);
        }
        return "user/diet-plans";
    }

    // Mark diet plan as completed
    @PostMapping("/diet-plans/{id}/complete")
    public String completeDietPlan(@PathVariable Long id) {
        dietPlanService.getPlanById(id).ifPresent(plan -> {
            plan.setCompleted(true);
            dietPlanService.savePlan(plan);
        });
        return "redirect:/user/diet-plans";
    }
} 
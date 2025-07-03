package com.fitness.controller;

import com.fitness.model.User;
import com.fitness.model.Workout;
import com.fitness.service.UserService;
import com.fitness.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {
    
    private final WorkoutService workoutService;
    private final UserService userService;
    
    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }
    
    @GetMapping
    public String listWorkouts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Workout> workouts = workoutService.getWorkoutsByUser(user);
        int totalCalories = workouts.stream()
            .mapToInt(w -> w.getCaloriesBurned() != null ? w.getCaloriesBurned() : 0)
            .sum();
        int totalDuration = workouts.stream()
            .mapToInt(w -> w.getDurationMinutes() != null ? w.getDurationMinutes() : 0)
            .sum();
        model.addAttribute("workouts", workouts);
        model.addAttribute("user", user);
        model.addAttribute("totalCalories", totalCalories);
        model.addAttribute("totalDuration", totalDuration);
        return "workout/list";
    }
    
    @GetMapping("/new")
    public String newWorkoutForm(Model model) {
        model.addAttribute("workout", new Workout());
        model.addAttribute("workoutTypes", Workout.WorkoutType.values());
        return "workout/form";
    }
    
    @PostMapping
    public String saveWorkout(@Valid @ModelAttribute Workout workout, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("workoutTypes", Workout.WorkoutType.values());
            return "workout/form";
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElse(null);
        
        if (user == null) {
            return "redirect:/login";
        }
        
        workout.setUser(user);
        workoutService.saveWorkout(workout);
        
        return "redirect:/workouts";
    }
    
    @GetMapping("/{id}")
    public String viewWorkout(@PathVariable Long id, Model model) {
        Workout workout = workoutService.getWorkoutById(id).orElse(null);
        if (workout == null) {
            return "redirect:/workouts";
        }
        
        model.addAttribute("workout", workout);
        return "workout/view";
    }
    
    @GetMapping("/{id}/edit")
    public String editWorkoutForm(@PathVariable Long id, Model model) {
        Workout workout = workoutService.getWorkoutById(id).orElse(null);
        if (workout == null) {
            return "redirect:/workouts";
        }
        
        model.addAttribute("workout", workout);
        model.addAttribute("workoutTypes", Workout.WorkoutType.values());
        return "workout/form";
    }
    
    @PostMapping("/{id}")
    public String updateWorkout(@PathVariable Long id, @Valid @ModelAttribute Workout workout, 
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("workoutTypes", Workout.WorkoutType.values());
            return "workout/form";
        }
        
        Workout existingWorkout = workoutService.getWorkoutById(id).orElse(null);
        if (existingWorkout == null) {
            return "redirect:/workouts";
        }
        
        // Update fields
        existingWorkout.setWorkoutName(workout.getWorkoutName());
        existingWorkout.setWorkoutType(workout.getWorkoutType());
        existingWorkout.setDurationMinutes(workout.getDurationMinutes());
        existingWorkout.setCaloriesBurned(workout.getCaloriesBurned());
        existingWorkout.setWorkoutDate(workout.getWorkoutDate());
        existingWorkout.setNotes(workout.getNotes());
        
        workoutService.updateWorkout(existingWorkout);
        
        return "redirect:/workouts";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return "redirect:/workouts";
    }
} 
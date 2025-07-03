package com.fitness.controller;

import com.fitness.model.User;
import com.fitness.model.Coach;
import com.fitness.model.Workout;
import com.fitness.model.WorkoutPlan;
import com.fitness.model.DietPlan;
import com.fitness.service.UserService;
import com.fitness.service.CoachService;
import com.fitness.service.WorkoutService;
import com.fitness.service.WorkoutPlanService;
import com.fitness.service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    
    private final UserService userService;
    private final CoachService coachService;
    private final WorkoutService workoutService;
    private final WorkoutPlanService workoutPlanService;
    private final DietPlanService dietPlanService;
    
    @Autowired
    public HomeController(UserService userService, CoachService coachService, WorkoutService workoutService, 
                         WorkoutPlanService workoutPlanService, DietPlanService dietPlanService) {
        this.userService = userService;
        this.coachService = coachService;
        this.workoutService = workoutService;
        this.workoutPlanService = workoutPlanService;
        this.dietPlanService = dietPlanService;
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            // User dashboard logic
            List<Workout> userWorkouts = workoutService.getWorkoutsByUser(user);
            long totalWorkouts = userWorkouts.size();
            
            // Calculate total calories burned
            long totalCalories = userWorkouts.stream()
                    .mapToLong(w -> w.getCaloriesBurned() != null ? w.getCaloriesBurned() : 0)
                    .sum();
            
            // Calculate total time in hours
            long totalTimeMinutes = userWorkouts.stream()
                    .mapToLong(w -> w.getDurationMinutes() != null ? w.getDurationMinutes() : 0)
                    .sum();
            long totalTimeHours = totalTimeMinutes / 60;
            
            long currentStreak = calculateCurrentStreak(userWorkouts);
            
            List<Workout> recentWorkouts = userWorkouts.stream()
                    .sorted((w1, w2) -> w2.getWorkoutDate().compareTo(w1.getWorkoutDate()))
                    .limit(5)
                    .toList();
            
            // Get user's workout and diet plans
            List<WorkoutPlan> workoutPlans = workoutPlanService.getPlansByUserId(user.getId());
            List<DietPlan> dietPlans = dietPlanService.getPlansByUserId(user.getId());
            
            // Prepare data for the last 7 days (workout count and calories per day)
            LocalDate today = LocalDate.now();
            Map<String, Integer> workoutCounts = new java.util.LinkedHashMap<>();
            Map<String, Integer> caloriesCounts = new java.util.LinkedHashMap<>();
            for (int i = 6; i >= 0; i--) {
                LocalDate day = today.minusDays(i);
                String label = day.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
                workoutCounts.put(label, 0);
                caloriesCounts.put(label, 0);
            }
            for (Workout w : userWorkouts) {
                LocalDate day = w.getWorkoutDate().toLocalDate();
                String label = day.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
                if (workoutCounts.containsKey(label)) {
                    workoutCounts.put(label, workoutCounts.get(label) + 1);
                    caloriesCounts.put(label, caloriesCounts.get(label) + (w.getCaloriesBurned() != null ? w.getCaloriesBurned() : 0));
                }
            }
            // Prepare data for workout type distribution
            Map<String, Integer> typeCounts = userWorkouts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    w -> w.getWorkoutType() != null ? w.getWorkoutType().name() : "OTHER",
                    java.util.stream.Collectors.summingInt(w -> 1)
                ));
            // Add to model for charts
            model.addAttribute("workoutLabels", workoutCounts.keySet());
            model.addAttribute("workoutCounts", workoutCounts.values());
            model.addAttribute("caloriesCounts", caloriesCounts.values());
            model.addAttribute("workoutTypeLabels", typeCounts.keySet());
            model.addAttribute("workoutTypeCounts", typeCounts.values());
            
            model.addAttribute("user", user);
            model.addAttribute("workoutCount", totalWorkouts);
            model.addAttribute("totalCalories", totalCalories);
            model.addAttribute("totalTime", totalTimeHours);
            model.addAttribute("currentStreak", currentStreak);
            model.addAttribute("recentWorkouts", recentWorkouts);
            model.addAttribute("workoutPlans", workoutPlans);
            model.addAttribute("dietPlans", dietPlans);
            return "dashboard";
        }

        Coach coach = coachService.getCoachByUsername(username).orElse(null);
        if (coach != null) {
            model.addAttribute("coach", coach);
            // Add coach-specific attributes here as needed
            return "coach-dashboard";
        }

        return "redirect:/login";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/coach-dashboard")
    public String coachDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Coach coach = coachService.getCoachByUsername(username).orElse(null);
        if (coach != null) {
            model.addAttribute("coach", coach);
            // Add coach-specific attributes here as needed
            return "coach-dashboard";
        }
        return "redirect:/login";
    }
    
    private long calculateCurrentStreak(List<Workout> workouts) {
        if (workouts.isEmpty()) {
            return 0;
        }
        List<Workout> sortedWorkouts = workouts.stream()
                .sorted((w1, w2) -> w2.getWorkoutDate().compareTo(w1.getWorkoutDate()))
                .toList();
        LocalDate currentDate = LocalDate.now();
        long streak = 0;
        for (Workout workout : sortedWorkouts) {
            LocalDate workoutDate = workout.getWorkoutDate().toLocalDate();
            long daysDiff = ChronoUnit.DAYS.between(workoutDate, currentDate);
            if (daysDiff == streak) {
                streak++;
            } else if (daysDiff > streak) {
                break;
            }
        }
        return streak;
    }
} 
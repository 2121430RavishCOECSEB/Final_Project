package com.fitness.controller;

import com.fitness.model.User;
import com.fitness.model.Coach;
import com.fitness.service.UserService;
import com.fitness.service.CoachService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    
    private final UserService userService;
    private final CoachService coachService;
    
    @Autowired
    public AuthController(UserService userService, CoachService coachService) {
        this.userService = userService;
        this.coachService = coachService;
    }
    
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("genders", User.Gender.values());
        model.addAttribute("goals", User.Goal.values());
        return "auth/register";
    }
    
    @GetMapping("/coach/register")
    public String coachRegisterForm(Model model) {
        model.addAttribute("coach", new Coach());
        model.addAttribute("genders", Coach.Gender.values());
        return "auth/coach-register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, 
                              Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("goals", User.Goal.values());
            return "auth/register";
        }
        
        // Check if username already exists (in both users and coaches)
        if (userService.existsByUsername(user.getUsername()) || 
            coachService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("goals", User.Goal.values());
            return "auth/register";
        }
        
        // Check if email already exists (in both users and coaches)
        if (userService.existsByEmail(user.getEmail()) || 
            coachService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("goals", User.Goal.values());
            return "auth/register";
        }
        
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "User registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("username", "error.user", "Registration failed. Please try again.");
            model.addAttribute("genders", User.Gender.values());
            model.addAttribute("goals", User.Goal.values());
            return "auth/register";
        }
    }
    
    @PostMapping("/coach/register")
    public String registerCoach(@Valid @ModelAttribute Coach coach, BindingResult result, 
                               Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("genders", Coach.Gender.values());
            return "auth/coach-register";
        }
        
        // Check if username already exists (in both users and coaches)
        if (userService.existsByUsername(coach.getUsername()) || 
            coachService.existsByUsername(coach.getUsername())) {
            result.rejectValue("username", "error.coach", "Username already exists");
            model.addAttribute("genders", Coach.Gender.values());
            return "auth/coach-register";
        }
        
        // Check if email already exists (in both users and coaches)
        if (userService.existsByEmail(coach.getEmail()) || 
            coachService.existsByEmail(coach.getEmail())) {
            result.rejectValue("email", "error.coach", "Email already exists");
            model.addAttribute("genders", Coach.Gender.values());
            return "auth/coach-register";
        }
        
        try {
            coachService.saveCoach(coach);
            redirectAttributes.addFlashAttribute("success", "Coach registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("username", "error.coach", "Coach registration failed. Please try again.");
            model.addAttribute("genders", Coach.Gender.values());
            return "auth/coach-register";
        }
    }
    
    @PostMapping("/login-success")
    @ResponseBody
    public Map<String, Object> loginSuccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Map<String, Object> response = new HashMap<>();
        
        // Try to find as user first
        User user = userService.getUserByUsername(username).orElse(null);
        if (user != null) {
            response.put("success", true);
            response.put("message", "Login successful!");
            response.put("role", "USER");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            return response;
        }
        
        // If not found as user, try to find as coach
        Coach coach = coachService.getCoachByUsername(username).orElse(null);
        if (coach != null) {
            response.put("success", true);
            response.put("message", "Login successful!");
            response.put("role", "COACH");
            response.put("username", coach.getUsername());
            response.put("email", coach.getEmail());
            response.put("firstName", coach.getFirstName());
            response.put("lastName", coach.getLastName());
            response.put("specialization", coach.getSpecialization());
            response.put("experienceYears", coach.getExperienceYears());
            response.put("hourlyRate", coach.getHourlyRate());
            return response;
        }
        
        response.put("success", false);
        response.put("message", "User not found");
        return response;
    }
} 
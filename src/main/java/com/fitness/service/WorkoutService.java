package com.fitness.service;

import com.fitness.model.Workout;
import com.fitness.model.User;
import com.fitness.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {
    
    private final WorkoutRepository workoutRepository;
    
    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }
    
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }
    
    public Optional<Workout> getWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }
    
    public List<Workout> getWorkoutsByUser(User user) {
        return workoutRepository.findByUserOrderByWorkoutDateDesc(user);
    }
    
    public List<Workout> getWorkoutsByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return workoutRepository.findByUserAndWorkoutDateBetweenOrderByWorkoutDateDesc(user, startDate, endDate);
    }
    
    public List<Workout> getRecentWorkoutsByUser(User user, LocalDateTime startDate) {
        return workoutRepository.findRecentWorkoutsByUser(user, startDate);
    }
    
    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }
    
    public Workout updateWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }
    
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
    
    public Long getWorkoutCountByUserSince(User user, LocalDateTime startDate) {
        return workoutRepository.countWorkoutsByUserSince(user, startDate);
    }
    
    public Integer getTotalCaloriesBurnedByUserSince(User user, LocalDateTime startDate) {
        Integer totalCalories = workoutRepository.sumCaloriesBurnedByUserSince(user, startDate);
        return totalCalories != null ? totalCalories : 0;
    }
    
    public List<Workout> getWorkoutsThisWeek(User user) {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        return getRecentWorkoutsByUser(user, startOfWeek);
    }
    
    public List<Workout> getWorkoutsThisMonth(User user) {
        LocalDateTime startOfMonth = LocalDateTime.now().minusDays(30);
        return getRecentWorkoutsByUser(user, startOfMonth);
    }
} 
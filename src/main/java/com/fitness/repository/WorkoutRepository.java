package com.fitness.repository;

import com.fitness.model.Workout;
import com.fitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    
    List<Workout> findByUserOrderByWorkoutDateDesc(User user);
    
    List<Workout> findByUserAndWorkoutDateBetweenOrderByWorkoutDateDesc(
        User user, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT w FROM Workout w WHERE w.user = :user AND w.workoutDate >= :startDate ORDER BY w.workoutDate DESC")
    List<Workout> findRecentWorkoutsByUser(@Param("user") User user, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user = :user AND w.workoutDate >= :startDate")
    Long countWorkoutsByUserSince(@Param("user") User user, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT SUM(w.caloriesBurned) FROM Workout w WHERE w.user = :user AND w.workoutDate >= :startDate")
    Integer sumCaloriesBurnedByUserSince(@Param("user") User user, @Param("startDate") LocalDateTime startDate);
} 
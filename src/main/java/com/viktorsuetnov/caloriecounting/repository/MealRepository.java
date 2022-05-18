package com.viktorsuetnov.caloriecounting.repository;

import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT m FROM Meal m WHERE m.id = :mealId AND m.user.id = :userId")
    Optional<Meal> getMealByIdAndUserId(long mealId, long userId);

    @Query("SELECT m FROM Meal m WHERE m.user.username = :username")
    Optional<List<Meal>> getMealByUsername(String username);

    @Query("SELECT m FROM Meal m " +
            "WHERE m.user.id = :userId " +
            "AND m.dateTime >= :startDate " +
            "AND m.dateTime <= :endDate " +
            "ORDER BY m.dateTime DESC")
    Optional<List<Meal>> getBetween(LocalDate startDate, LocalDate endDate, long userId);
}

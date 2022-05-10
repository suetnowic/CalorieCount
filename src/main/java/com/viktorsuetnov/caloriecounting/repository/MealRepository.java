package com.viktorsuetnov.caloriecounting.repository;

import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> getMealById(Long id);
}

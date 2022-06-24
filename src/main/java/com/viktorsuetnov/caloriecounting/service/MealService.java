package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.dto.MealDTO;
import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface MealService {

    Meal createMeal(Meal meal, Principal principal);

    Meal getMeal(long mealId, Principal principal);

    List<Meal> getAll(Principal principal);

    Meal updateMeal(Meal meal, Meal mealIn);

    boolean delete(Long id, Principal principal);

    Optional<List<Meal>> getBetweenDates(LocalDate startDate, LocalDate endDate, long userId);

}

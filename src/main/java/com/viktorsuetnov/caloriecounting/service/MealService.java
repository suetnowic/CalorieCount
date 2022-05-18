package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.dto.MealDTO;
import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface MealService {

    Meal createMeal(Meal meal, Principal principal);

    Meal getMeal(long mealId, Principal principal);

    List<Meal> getAll(Principal principal);

    Meal updateMeal(Meal meal, Meal mealIn);

    boolean delete(Long id, Principal principal);

    List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate, long userId);

    List<MealDTO> getBetweenDatesAndTimes(LocalDate startDate, LocalTime startTime,
                                       LocalDate endDate, LocalTime endTime, Long userId);

}

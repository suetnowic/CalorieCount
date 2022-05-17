package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface MealService {

    Meal createMeal(Meal meal, Principal principal);

    Meal getMeal(long mealId, long userId);

    List<Meal> getAll(long userId);

    void updateMeal(Meal meal, Principal principal);

    void delete(long mealId);

    List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate, long userId);



}

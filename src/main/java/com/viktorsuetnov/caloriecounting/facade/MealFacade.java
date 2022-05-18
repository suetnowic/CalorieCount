package com.viktorsuetnov.caloriecounting.facade;

import com.viktorsuetnov.caloriecounting.dto.MealDTO;
import com.viktorsuetnov.caloriecounting.model.Meal;
import org.springframework.stereotype.Component;

@Component
public class MealFacade {

    public MealDTO mealToMealDTO(Meal meal) {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setId(meal.getId());
        mealDTO.setDateTime(meal.getDateTime());
        mealDTO.setDescription(meal.getDescription());
        mealDTO.setCalories(meal.getCalories());
        return mealDTO;
    }
}

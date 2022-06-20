package com.viktorsuetnov.caloriecounting.mapper;

import com.viktorsuetnov.caloriecounting.dto.MealDTO;
import com.viktorsuetnov.caloriecounting.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MealMapper {
    MealMapper INSTANCE = Mappers.getMapper(MealMapper.class);
    MealDTO toDTO(Meal meal);
}

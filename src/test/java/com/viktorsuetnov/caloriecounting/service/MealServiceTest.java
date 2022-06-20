package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.Meal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MealServiceTest {

    private MockMvc mockMvc;
    @Spy
    private MealServiceImpl mealServiceMock;
    @InjectMocks
    Meal meal = new Meal();
    @InjectMocks
    Principal principal;

    @Test
    public void findAll() {
        Meal m = new Meal(1L, LocalDateTime.of(2022, 5, 27, 8, 15), "Завтрак", 650);
        when(mealServiceMock.getMeal(meal.getId(), principal)).thenReturn(m);
    }
}

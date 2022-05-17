package com.viktorsuetnov.caloriecounting.dto;

import java.time.LocalDateTime;

public class MealDTO {

    private Long id;
    private LocalDateTime dateTime;
    private String description;
    private Integer calories;
    private Boolean excess;
}

package com.viktorsuetnov.caloriecounting.dto;

import java.time.LocalDateTime;

public class MealDTO {

    private Long id;
    private LocalDateTime dateTime;
    private String description;
    private Integer calories;
    private Boolean excess;

    public MealDTO() {
    }

    public MealDTO(Long id, LocalDateTime dateTime, String description, Integer calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public MealDTO(Long id, LocalDateTime dateTime, String description, Integer calories, Boolean excess) {
        this(id, dateTime, description, calories);
        this.excess = excess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Boolean getExcess() {
        return excess;
    }

    public void setExcess(Boolean excess) {
        this.excess = excess;
    }
}

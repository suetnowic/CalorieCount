package com.viktorsuetnov.caloriecounting.controller;

import com.viktorsuetnov.caloriecounting.dto.MealDTO;
import com.viktorsuetnov.caloriecounting.facade.MealFacade;
import com.viktorsuetnov.caloriecounting.model.Meal;
import com.viktorsuetnov.caloriecounting.payload.response.MessageResponse;
import com.viktorsuetnov.caloriecounting.service.MealService;
import com.viktorsuetnov.caloriecounting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/meal")
@Tag(name = "Контролер приема пищи", description = "Позволяет создать, обновить, удалить прием пищи, а также получить список или конкретный прием пищи")
public class MealController {

    private final MealService mealService;
    private final MealFacade mealFacade;

    @Autowired
    public MealController(MealService mealService, MealFacade mealFacade) {
        this.mealService = mealService;
        this.mealFacade = mealFacade;
    }

    @Operation(summary = "Добавление приема пищи")
    @PostMapping("/")
    public ResponseEntity<MealDTO> createMeal(@RequestBody Meal mealIn, Principal principal) {
        Meal meal = mealService.createMeal(mealIn, principal);
        MealDTO mealDTO = mealFacade.mealToMealDTO(meal);
        return new ResponseEntity<>(mealDTO, HttpStatus.OK);
    }

    @Operation(summary = "Получение всех приемов пищи текущего пользователя")
    @GetMapping("/all")
    public ResponseEntity<List<MealDTO>> getAll(Principal principal) {
        List<Meal> meals = mealService.getAll(principal);
        List<MealDTO> mealDTOs = meals.stream()
                .map(meal -> mealFacade.mealToMealDTO(meal))
                .collect(Collectors.toList());
        return new ResponseEntity<>(mealDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Получение приема пищи по id")
    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMealById(@PathVariable Long id, Principal principal) {
        Meal meal = mealService.getMeal(id, principal);
        MealDTO mealDTO = mealFacade.mealToMealDTO(meal);
        return new ResponseEntity<>(mealDTO, HttpStatus.OK);
    }

    @Operation(summary = "Обновление приема пищи")
    @PutMapping("/{id}")
    public ResponseEntity<MealDTO> updateMeal(@PathVariable Long id, @RequestBody Meal mealIn, Principal principal) {
        Meal meal = mealService.getMeal(id, principal);
        Meal updateMeal = mealService.updateMeal(meal, mealIn);
        MealDTO mealDTO = mealFacade.mealToMealDTO(updateMeal);
        return new ResponseEntity<>(mealDTO, HttpStatus.OK);
    }

    @Operation(summary = "Удаление приема пищи")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id, Principal principal) {
        return mealService.delete(id, principal) ?
                ResponseEntity.ok(new MessageResponse("Meal deleted successfully")) :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Meal for deleted not found");
    }
}

package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.exception.MealNotFoundException;
import com.viktorsuetnov.caloriecounting.exception.UserNotFoundException;
import com.viktorsuetnov.caloriecounting.model.Meal;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.MealRepository;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {

    public static final Logger LOG = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal createMeal(Meal meal, Principal principal) {
        User user = getUserFromPrincipal(principal);
        meal.setUser(user);
        LOG.info("Create new Meal for user with username {}", user.getUsername());
        return mealRepository.save(meal);
    }

    @Override
    public Meal getMeal(long mealId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        return mealRepository.getMealByIdAndUserId(mealId, user.getId()).orElseThrow(() ->
                new MealNotFoundException("Meal for user " + user.getUsername() + " not found"));
    }

    @Override
    public List<Meal> getAll(Principal principal) {
        User user = getUserFromPrincipal(principal);
        return mealRepository.getMealByUsername(user.getUsername()).orElseThrow(() ->
                new MealNotFoundException("Meals for user " + user.getUsername() + " not found"));
    }

    @Override
    public Meal updateMeal(Meal meal, Meal mealIn) {
        meal.setId(mealIn.getId());
        meal.setDateTime(mealIn.getDateTime());
        meal.setDescription(mealIn.getDescription());
        meal.setCalories(mealIn.getCalories());
        LOG.info("Saving the updated meal");
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(Long mealId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        Meal meal = mealRepository.getMealByIdAndUserId(mealId, user.getId()).orElseThrow(() ->
                new MealNotFoundException("Meal for user " + user.getUsername() + " not found"));
        if (isEquals(meal, user)) {
            LOG.info("Delete meal with {} for username {}", mealId, user.getUsername());
            mealRepository.delete(meal);
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<Meal>> getBetweenDates(LocalDate startDate, LocalDate endDate, long userId) {
        return getBetweenDatesAndTimes(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX), userId);
    }

    private Optional<List<Meal>> getBetweenDatesAndTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, long userId) {
        return mealRepository.getBetween(startDateTime, endDateTime, userId);
    }

    private boolean isEquals(Meal meal, User user) {
        return meal.getUser().equals(user);
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.getUserByUsername(username).orElseThrow(() ->
                new UserNotFoundException("Username with username " + username + " not found"));
    }
}

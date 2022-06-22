package com.viktorsuetnov.caloriecounting;

import com.viktorsuetnov.caloriecounting.model.Meal;
import com.viktorsuetnov.caloriecounting.model.Role;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.request.SignupRequest;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestData {

    public static final Meal MEAL1 = new Meal(1L, LocalDateTime.of(2022, 6, 19, 7, 30), "завтрак", 500);
    public static final Meal MEAL2 = new Meal(2L, LocalDateTime.of(2022, 6, 19, 13, 0), "обед", 800);
    public static final Meal MEAL3 = new Meal(3L, LocalDateTime.of(2022, 6, 19, 19, 30), "ужин", 650);
    public static final Meal MEAL4 = new Meal(4L, LocalDateTime.of(2022, 6, 20, 8, 0), "завтрак", 350);
    public static final Meal MEAL5 = new Meal(5L, LocalDateTime.of(2022, 6, 20, 11, 30), "второй завтрак", 150);
    public static final Meal MEAL6 = new Meal(6L, LocalDateTime.of(2022, 6, 20, 13, 30), "обед", 750);
    public static final Meal MEAL7 = new Meal(7L, LocalDateTime.of(2022, 6, 20, 20, 00), "ужин", 550);

    public static final List<Meal> MEALS = Arrays.asList(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6, MEAL7);

    public static final List<GrantedAuthority> ROLES = Stream.of(Role.ROLE_USER).collect(Collectors.toList());

    public static final User USER1 = new User(1L, "user1", "user1@gmail.com", "user1user", ROLES);
    public static final User USER2 = new User(2L, "user2", "user2@gmail.com", "user2user", ROLES);

    public static final List<User> USERS = Arrays.asList(USER1);

    public static final SignupRequest SIGNUP_REQUEST = SignupRequest.builder()
            .username("user3")
            .email("user3@mail.io")
            .password("user3user")
            .confirmPassword("user3user")
            .caloriesPerDay(1500)
            .build();
}

package com.viktorsuetnov.caloriecounting.controller;

import com.viktorsuetnov.caloriecounting.dto.UserDTO;
import com.viktorsuetnov.caloriecounting.facade.UserFacade;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.response.MessageResponse;
import com.viktorsuetnov.caloriecounting.service.UserService;
import com.viktorsuetnov.caloriecounting.validations.ResponseErrorValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Контролер пользователя", description = "Позволяет получить: текущего пользователя, профиль; " +
        "обновить профиль, активировать аккаунт, получить всех пользователей")
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidator responseErrorValidator;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidator responseErrorValidator) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidator = responseErrorValidator;
    }

    @Operation(summary = "Получение текущего пользователя")
    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(summary = "Получение всех пользователей")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> userFacade.userToUserDTO(user))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Получение профиля пользователя")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(Principal principal, @PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(summary = "Обновление пользователя")
    @PutMapping("/")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User userIn,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        User user = userService.updateProfile(userIn, principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(summary = "Активация аккаунта")
    @GetMapping("/activate/{code}")
    public ResponseEntity<Object> activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return ResponseEntity.ok(new MessageResponse("User activated successfully"));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Activation code is not found");
    }
}

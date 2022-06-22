package com.viktorsuetnov.caloriecounting.controller;

import com.viktorsuetnov.caloriecounting.dto.UserDTO;
import com.viktorsuetnov.caloriecounting.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.viktorsuetnov.caloriecounting.TestData.USERS;

@SpringBootTest
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Test
    public void getAllUsersTest() {
        Mockito.when(userService.getAllUsers()).thenReturn(USERS);
        ResponseEntity<List<UserDTO>> allUsers = userController.getAllUsers();
        Assertions.assertNotNull(allUsers);
    }
}

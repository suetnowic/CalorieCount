package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.exception.UserNotFoundException;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.viktorsuetnov.caloriecounting.TestData.USER1;
import static com.viktorsuetnov.caloriecounting.TestData.USERS;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(USERS);
        List<User> found = userService.getAllUsers();
        assertThat(found.size()).isGreaterThan(0);
        assertThat(found).isNotNull();
        assertThat(found.get(0)).isEqualTo(USER1);
    }

    @Test
    public void getUserById() {
        Mockito.when(userRepository.getUserById(1L)).thenReturn(Optional.of(USERS.get(0)));
        Assertions.assertEquals(userService.getUserById(1L), USERS.get(0));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Mockito.when(userRepository.getUserById(2L)).thenThrow(new UserNotFoundException("User with id = 2 not found"));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    public void getUserByEmail() {
        Mockito.when(userRepository.getUserByEmail("user1@mail.io")).thenReturn(Optional.of(USERS.get(0)));
        Assertions.assertEquals(userService.getUserByEmail("user1@mail.io"), USERS.get(0));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Mockito.when(userRepository.getUserByEmail("dummy@mail.io")).thenThrow(new UserNotFoundException("User with email = dummy@mail.io not found"));
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("dummy@mail.io"));
    }
}

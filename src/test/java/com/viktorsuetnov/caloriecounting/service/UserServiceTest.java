package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.exception.UserNotFoundException;
import com.viktorsuetnov.caloriecounting.model.Role;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.viktorsuetnov.caloriecounting.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private MailSender mailSender;

    @BeforeEach
    public void init() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void createUser() {
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
        User user = userService.createUser(SIGNUP_REQUEST);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.ROLE_USER)));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

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

    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("activate");
        Mockito.when(userRepository.getUserByActivationCode("activate")).thenReturn(Optional.of(user));
        boolean isActivate = userService.activateUser("activate");
        Assertions.assertTrue(isActivate);
        Assertions.assertNull(user.getActivationCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
}

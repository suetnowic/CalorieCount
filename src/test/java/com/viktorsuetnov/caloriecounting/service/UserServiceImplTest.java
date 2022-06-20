package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private MailSender mailSender;
    @InjectMocks
    private UserService userService;

    @Test
    public void create() {

    }
}

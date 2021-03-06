package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.request.SignupRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {

    User createUser(SignupRequest userIn);

    List<User> getAllUsers();

    User getUserByEmail(String username);

    User getCurrentUser(Principal principal);

    User getUserById(Long id);

    User updateProfile(User userIn, Principal principal);

    boolean activateUser(String code);
}

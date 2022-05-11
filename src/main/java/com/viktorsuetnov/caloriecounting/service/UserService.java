package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getCurrentUser(Principal principal);

    User getUserById(Long id);

    User updateProfile(User user, Principal principal);
}

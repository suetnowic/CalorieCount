package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.exception.UserExistException;
import com.viktorsuetnov.caloriecounting.model.Role;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.request.SignupRequest;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        try {
            LOG.info("Saving user with {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception exception) {
            LOG.error("Error during registration {}", exception.getMessage());
            throw new UserExistException("The user with " + userIn.getUsername() + "already exist");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User updateProfile(User user, Principal principal) {
        return null;
    }
}

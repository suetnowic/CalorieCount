package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.exception.UserExistException;
import com.viktorsuetnov.caloriecounting.exception.UserNotFoundException;
import com.viktorsuetnov.caloriecounting.model.Role;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.payload.request.SignupRequest;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailSender mailSender;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setUsername(userIn.getUsername());
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        try {
            LOG.info("Saving user with {}", userIn.getEmail());
            userRepository.save(user);
            sendMessage(user);
            return user;
        } catch (Exception exception) {
            LOG.error("Error during registration {}", exception.getMessage());
            throw new UserExistException("The user with " + userIn.getUsername() + "already exist");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
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

    @Override
    public boolean activateUser(String code) {
        LOG.info("Activate user by " + code);
        User user = userRepository.getUserByActivationCode(code);
        if (user == null) {
            LOG.info("User by activation code is not found " + code);
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        LOG.info("User activated successfully");
        userRepository.save(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n" +
                            "Welcome to Calorie Counting application. Please visit the next link: http://%s/api/user/activate/%s",
                    user.getUsername(), hostname, user.getActivationCode());
            LOG.info("Send message to user with email: " + user.getEmail());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
}

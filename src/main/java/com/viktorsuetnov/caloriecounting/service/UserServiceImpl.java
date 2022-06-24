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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Integer DEFAULT_CALORIES_PER_DAY = 2000;

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
        user.setCaloriesPerDay(userIn.getCaloriesPerDay() == null ? DEFAULT_CALORIES_PER_DAY : userIn.getCaloriesPerDay());
        user.getRoles().add(Role.ROLE_USER);
        try {
            LOG.info("Saving user with {}", userIn.getEmail());
            userRepository.save(user);
            LOG.info("Send email message...");
            sendMessage(user);
            return user;
        } catch (Exception exception) {
            LOG.error("Error during registration {}", exception.getMessage());
            throw new UserExistException("The user with username " + userIn.getUsername() + " already exist");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return getUserFromPrincipal(principal);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User updateProfile(User userIn, Principal principal) {
        User user = getUserFromPrincipal(principal);
        String currentUserEmail = user.getEmail();

        String newEmail = userIn.getEmail();
        String newPassword = userIn.getPassword();

        final boolean isEmailUpdated = changeEmail(currentUserEmail, newEmail, user);
        final boolean isPasswordUpdated = changePassword(newPassword, user);
        if (isEmailUpdated || isPasswordUpdated) {
            try {
                LOG.info("Saving the updated user");
                userRepository.save(user);
            } catch (Exception exception) {
                LOG.error("Error during update profile {}", exception.getMessage());
            }
        }
        return userRepository.save(user);
    }

    @Override
    public boolean activateUser(String code) {
        LOG.info("Activate user by " + code);
        User user = userRepository.getUserByActivationCode(code).orElse(null);
        if (user != null) {
            user.setActive(true);
            user.setActivationCode(null);
            LOG.info("User activated successfully");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n" +
                            "Welcome to Calorie Counting application. Please visit the next link: http://%s/api/user/activate/%s",
                    user.getUsername(), hostname, user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    private boolean changeEmail(String currentUserEmail, String newEmail, User user) {
        if ((!StringUtils.isEmpty(newEmail) && !newEmail.equals(currentUserEmail))
                || (!StringUtils.isEmpty(currentUserEmail) && !currentUserEmail.equals(newEmail))) {
            LOG.info("Update " + user.getUsername() + " email from " + currentUserEmail + " to " + newEmail);
            user.setEmail(newEmail);
            user.setActivationCode(UUID.randomUUID().toString());
            return true;
        }
        return false;
    }

    private boolean changePassword(String newPassword, User user) {
        if (StringUtils.hasText(newPassword)) {
            LOG.info("Update password for user {}...", user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.getUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username with " + username + " not found"));
    }
}

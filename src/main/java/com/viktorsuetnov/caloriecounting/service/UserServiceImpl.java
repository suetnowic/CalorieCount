package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found"));
        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.getUserById(id).orElse(null);
    }

    private static User build(User user) {
        List<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return new User(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
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

package com.viktorsuetnov.caloriecounting.service;

import com.viktorsuetnov.caloriecounting.model.Role;
import com.viktorsuetnov.caloriecounting.model.User;
import com.viktorsuetnov.caloriecounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(username);
        if (user != null && user.isEnabled()) {
            List<GrantedAuthority> authorities = getUserAuthority(user);
            return build(user, authorities);
        } else {
            throw new UsernameNotFoundException("User with " + username + " not found");
        }
    }

    public User loadUserById(Long id) {
        return userRepository.getUserById(id).orElse(null);
    }

    private List<GrantedAuthority> getUserAuthority(User user) {
        return user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    private static User build(User user, List<GrantedAuthority> authorities) {
        return new User(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }
}

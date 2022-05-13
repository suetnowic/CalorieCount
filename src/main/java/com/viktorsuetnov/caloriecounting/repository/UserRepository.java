package com.viktorsuetnov.caloriecounting.repository;

import com.viktorsuetnov.caloriecounting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserById(Long id);

    User getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    User getUserByActivationCode(String code);
}

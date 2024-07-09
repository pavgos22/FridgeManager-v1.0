package com.food.manager.entity;

import com.food.manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTestSuite {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.user@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @Transactional
    public void testCreateUser() {
        // When
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getUserId());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        // Given
        userRepository.save(user);
        User savedUser = userRepository.findById(user.getUserId()).orElseThrow();

        // When
        savedUser.setFirstName("UpdatedFirstName");
        savedUser.setLastName("UpdatedLastName");
        savedUser.setEmail("updated.email@example.com");
        savedUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(savedUser);

        // Then
        User updatedUser = userRepository.findById(savedUser.getUserId()).orElseThrow();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedUser.getLastName()).isEqualTo("UpdatedLastName");
        assertThat(updatedUser.getEmail()).isEqualTo("updated.email@example.com");
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        // Given
        userRepository.save(user);
        User savedUser = userRepository.findById(user.getUserId()).orElseThrow();

        // When
        userRepository.delete(savedUser);

        // Then
        Optional<User> foundUser = userRepository.findById(savedUser.getUserId());
        assertThat(foundUser).isNotPresent();
    }
}

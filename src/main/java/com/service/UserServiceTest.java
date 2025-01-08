package com.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSignupSuccess() {
        // Arrange
        String userName = "owner";
        String userPass = "password123";
        String userRole = "Owner";

        User user = new User();
        user.setUserName(userName);
        user.setUserRole(userRole);

        when(passwordEncoder.encode(userPass)).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        boolean result = userService.signup(userName, userPass, userRole);

        // Assert
        assertTrue(result, "Owner should be able to sign up");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupFail() {
        // Arrange
        String userName = "customer";
        String userPass = "password123";
        String userRole = "Customer";

        // Act
        boolean result = userService.signup(userName, userPass, userRole);

        // Assert
        assertFalse(result, "Only Owner should be able to sign up");
        verify(userRepository, never()).save(any(User.class));
    }
}

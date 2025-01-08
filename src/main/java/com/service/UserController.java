package com.service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

    // Constructor to inject the service
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        boolean result = userService.signup(user.getUserName(), user.getUserPass(), user.getUserRole());
        return result ? ResponseEntity.status(HttpStatus.CREATED).body("Signup successful")
                      : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Signup failed. Only owner can create accounts.");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean result = userService.login(user.getUserName(), user.getUserPass());
        return result ? ResponseEntity.ok("Login successful")
                      : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }
}

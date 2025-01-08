package com.service;
import org.springframework.web.bind.annotation.*;

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
    public String signup(@RequestParam String userName,
                         @RequestParam String userPass,
                         @RequestParam String userRole) {
        boolean result = userService.signup(userName, userPass, userRole);
        return result ? "Signup successful" : "Signup failed. Only owner can create accounts.";
    }

    // Login endpoint
    @PostMapping("/login")
    public String login(@RequestParam String userName,
                        @RequestParam String userPass) {
        boolean result = userService.login(userName, userPass);
        return result ? "Login successful" : "Invalid username or password.";
    }
}

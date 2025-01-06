package com.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    // Constructor to inject the repository and password encoder
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Create a password encoder
    }

    // Signup method (only Owner can create accounts)
    public boolean signup(String userName, String userPass, String userRole) {
        if ("Owner".equals(userRole)) {
            // Owner can create accounts
            User newUser = new User();
            newUser.setUserName(userName);

            // Hash the password before storing it
            String hashedPassword = passwordEncoder.encode(userPass);
            newUser.setUserPass(hashedPassword);

            newUser.setUserRole(userRole);
            newUser.setAccStatus("active"); // Default to active
            userRepository.save(newUser);
            return true;
        }
        return false; // Only Owner can create accounts
    }

    // Login method (authenticate user)
    public boolean login(String userName, String userPass) {
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            // Use the password encoder to match the hashed password
            return passwordEncoder.matches(userPass, user.getUserPass());
        }
        return false; // Invalid credentials
    }
}

package com.service;

public class UserService {
	 private UserRepository userRepository;

	    // Constructor to inject the repository
	    public UserService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	    // Signup method (only Owner can create accounts)
	    public boolean signup(String userName, String userPass, String userRole) {
	        if ("Owner".equals(userRole)) {
	            // Owner can create accounts
	            User newUser = new User();
	            newUser.setUserName(userName);
	            newUser.setUserPass(userPass);
	            newUser.setUserRole(userRole);
	            newUser.setAccStatus("active");  // Default to active
	            userRepository.save(newUser);
	            return true;
	        }
	        return false; // Only Owner can create accounts
	    }

	    // Login method (authenticate user)
	    public boolean login(String userName, String userPass) {
	        User user = userRepository.findByUserName(userName);
	        if (user != null && user.getUserPass().equals(userPass)) {
	            // User is authenticated
	            return true;
	        }
	        return false; // Invalid credentials
	    }
}

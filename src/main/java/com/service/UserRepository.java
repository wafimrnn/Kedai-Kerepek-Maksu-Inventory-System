package com.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query method to find a user by their username
    User findByUserName(String userName);
}

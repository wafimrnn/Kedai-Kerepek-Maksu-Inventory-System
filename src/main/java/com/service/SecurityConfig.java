package com.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF for testing; enable in production.
            .authorizeRequests()
            .antMatchers("/api/users/signup").hasAuthority("Owner") // Only 'owner' role can access signup.
            .antMatchers("/api/users/login").permitAll() // Anyone can access login.
            .anyRequest().authenticated() // All other requests require authentication.
            .and()
            .httpBasic(); // Use basic HTTP authentication for simplicity.

        return http.build();
    }
}

public boolean signup(String userName, String userPass, String userRole) {
    if ("Owner".equals(userRole)) {
        User newUser = new User();
        newUser.setUserName(userName);

        // Hash the password before storing it
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userPass);
        newUser.setUserPass(hashedPassword);

        newUser.setUserRole(userRole);
        newUser.setAccStatus("active");
        userRepository.save(newUser);
        return true;
    }
    return false;
}
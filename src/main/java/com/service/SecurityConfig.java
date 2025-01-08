package com.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        http.csrf().disable() // Disable CSRF for simplicity.
            .authorizeRequests()
            .requestMatchers("/api/users/signup").hasAuthority("Owner") // Restrict signup to Owner.
            .requestMatchers("/api/users/login").permitAll() // Allow public access to login.
            .anyRequest().authenticated() // All other requests require authentication.
            .and()
            .oauth2ResourceServer().jwt(); // Enable JWT support.

        return http.build();
    }
}

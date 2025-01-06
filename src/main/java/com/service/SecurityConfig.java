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
        http.csrf().disable() // Disable CSRF for testing; enable in production.
            .authorizeRequests()
            .antMatchers("/api/users/signup").hasAuthority("Owner") // Only 'Owner' role can access signup.
            .antMatchers("/api/users/login").permitAll() // Anyone can access login.
            .anyRequest().authenticated() // All other requests require authentication.
            .and()
            .httpBasic(); // Use basic HTTP authentication for simplicity.

        return http.build();
    }
}

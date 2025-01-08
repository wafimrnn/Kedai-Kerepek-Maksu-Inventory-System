package com.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void testGenerateAndValidateToken() {
        String username = "testUser";
        String token = JwtUtil.generateToken(username);

        assertNotNull(token, "Token should be generated");
        String validatedUsername = JwtUtil.validateToken(token);

        assertEquals(username, validatedUsername, "Validated username should match");
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "invalidToken";

        String result = JwtUtil.validateToken(invalidToken);

        assertNull(result, "Invalid token should return null");
    }
}

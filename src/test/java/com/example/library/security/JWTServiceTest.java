package com.example.library.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "testSecretKeyForJWTTestingPurposesOnly123456");
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000L);
    }

    @Test
    void generateToken_shouldContainCorrectUsername() {
        String token = jwtService.generateToken("testuser", "USER");
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    void isTokenExpired_shouldReturnFalse_forFreshToken() {
        String token = jwtService.generateToken("testuser", "USER");

        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void extractExpiration_shouldReturnFutureDate() {
        String token = jwtService.generateToken("testuser", "USER");

        assertTrue(jwtService.extractExpiration(token).after(new java.util.Date()));
    }
}
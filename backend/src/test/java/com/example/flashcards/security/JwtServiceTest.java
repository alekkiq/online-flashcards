package com.example.flashcards.security;

import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;
    private static final String TEST_SECRET = "test-secret-key-that-is-long-enough-for-hs256-algorithm-minimum-256-bits";
    private static final Long TEST_EXPIRATION = 3600000L; // 1 hour
    private User testUser;

    @BeforeEach
    void setUp() {
        this.jwtService = new JwtService();
        ReflectionTestUtils.setField(this.jwtService, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(this.jwtService, "expiration", TEST_EXPIRATION);

        this.testUser = new User("testuser", "test@example.com", "password");
    }

    @Test
    @DisplayName("generateToken(): should create a valid JWT token")
    void generateToken_ShouldCreateValidToken() {
        String token = this.jwtService.generateToken(this.testUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    @DisplayName("generateToken(): should include username in token")
    void generateToken_ShouldIncludeUsername() {
        String token = this.jwtService.generateToken(this.testUser);
        String username = this.jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("generateToken(): should include role claim in token")
    void generateToken_ShouldIncludeRoleClaim() {
        String token = this.jwtService.generateToken(this.testUser);

        Key key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("STUDENT", claims.get("role"));
    }

    @Test
    @DisplayName("generateToken(): should set expiration time")
    void generateToken_ShouldSetExpirationTime() {
        String token = this.jwtService.generateToken(this.testUser);

        Key key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
        Date expiration = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("extractUsername(): should return correct username from token")
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = this.jwtService.generateToken(this.testUser);
        String username = this.jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("extractUsername(): should throw exception for invalid token")
    void extractUsername_WithInvalidToken_ShouldThrowException() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> this.jwtService.extractUsername(invalidToken));
    }

    @Test
    @DisplayName("isExpiredToken(): should return false for valid token")
    void isExpiredToken_WithValidToken_ShouldReturnFalse() {
        String token = this.jwtService.generateToken(this.testUser);
        boolean isExpired = this.jwtService.isExpiredToken(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("isExpiredToken(): should return true for expired token")
    void isExpiredToken_WithExpiredToken_ShouldReturnTrue() {
        ReflectionTestUtils.setField(this.jwtService, "expiration", -1000L);
        String token = this.jwtService.generateToken(this.testUser);

        ReflectionTestUtils.setField(this.jwtService, "expiration", TEST_EXPIRATION);
        boolean isExpired = this.jwtService.isExpiredToken(token);

        assertTrue(isExpired);
    }

    @Test
    @DisplayName("isValidToken(): should return true for valid token and matching user")
    void isValidToken_WithValidTokenAndMatchingUser_ShouldReturnTrue() {
        String token = this.jwtService.generateToken(this.testUser);
        UserDetails userDetails = new CustomUserDetails(this.testUser);

        boolean isValid = this.jwtService.isValidToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("isValidToken(): should return false for valid token and different user")
    void isValidToken_WithValidTokenAndDifferentUser_ShouldReturnFalse() {
        String token = this.jwtService.generateToken(this.testUser);

        User differentUser = new User("diffUser", "different@example.com", "diffpassword");

        UserDetails userDetails = new CustomUserDetails(differentUser);
        boolean isValid = this.jwtService.isValidToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("isValidToken(): should return false for expired token")
    void isValidToken_WithExpiredToken_ShouldReturnFalse() {
        ReflectionTestUtils.setField(this.jwtService, "expiration", -1000L);
        String token = this.jwtService.generateToken(this.testUser);

        ReflectionTestUtils.setField(this.jwtService, "expiration", TEST_EXPIRATION);
        UserDetails userDetails = new CustomUserDetails(this.testUser);
        boolean isValid = this.jwtService.isValidToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("generateToken(): should include ADMIN role in token when user is admin")
    void generateToken_WithAdminRole_ShouldIncludeAdminRole() {
        User adminUser = new User("admin", "admin@example.com", "adminpassword");
        adminUser.setRole(UserRole.ADMIN);

        String token = this.jwtService.generateToken(adminUser);

        Key key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        assertEquals("ADMIN", claims.get("role"));
    }
}
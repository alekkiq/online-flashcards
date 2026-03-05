package com.example.flashcards.security;

import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {
    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeAll
    static void initAll() {
        System.out.println("CustomUserDetailsService test start");
    }

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.customUserDetailsService = new CustomUserDetailsService(this.userRepository);
    }

    @Test
    @DisplayName("loadUserByUsername(): returns CustomUserDetails when user found")
    void loadUserByUsername_success() {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(1L);

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = this.customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertInstanceOf(CustomUserDetails.class, result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    @DisplayName("loadUserByUsername(): returns correct userId via CustomUserDetails")
    void loadUserByUsername_returnsCorrectUserId() {
        User user = new User("testuser", "test@test.com", "password");
        user.setUserId(42L);

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        CustomUserDetails result = (CustomUserDetails) this.customUserDetailsService.loadUserByUsername("testuser");

        assertEquals(42L, result.getUserId());
    }

    @Test
    @DisplayName("loadUserByUsername(): throws UsernameNotFoundException when user not found")
    void loadUserByUsername_userNotFound_throwsException() {
        when(this.userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> this.customUserDetailsService.loadUserByUsername("nonexistent"));

        assertTrue(exception.getMessage().contains("nonexistent"));
    }
}


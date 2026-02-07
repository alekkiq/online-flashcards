package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.UnauthorizedException;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import com.example.flashcards.entity.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeAll
    static void initAll() {
        System.out.println("AuthService test start");
    }

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authService = new AuthService(this.userRepository, this.passwordEncoder);
    }

    @Test
    @DisplayName("register(): successful registration")
    void registerSuccess() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "newuser",
                "newuser@test.com",
                "password123"
        );

        when(this.userRepository.existsByUsername("newuser")).thenReturn(false);
        when(this.userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0);
            user.setUserId(1L);
            return user;
        });

        User result = this.authService.register(request);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("newuser@test.com", result.getEmail());
        assertTrue(this.passwordEncoder.matches("password123", result.getPassword()));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(this.userRepository, times(1)).save(captor.capture());
        assertEquals("newuser", captor.getValue().getUsername());
    }

    @Test
    @DisplayName("register(): duplicate username throws exception")
    void register_duplicateUsername_throwsException() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "existinguser",
                "new@test.com",
                "password123"
        );

        when(this.userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.authService.register(request));
        verify(this.userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register(): duplicate email throws exception")
    void register_duplicateEmail_throwsException() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "newuser",
                "existing@test.com",
                "password123"
        );

        when(this.userRepository.existsByUsername("newuser")).thenReturn(false);
        when(this.userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.authService.register(request));
        verify(this.userRepository, never()).save(any());
    }

    @Test
    @DisplayName("register(): password is hashed")
    void register_passwordIsHashed() {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "testuser",
                "test@test.com",
                "plainPassword"
        );

        when(this.userRepository.existsByUsername("testuser")).thenReturn(false);
        when(this.userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = this.authService.register(request);

        assertNotEquals("plainPassword", result.getPassword());
        assertTrue(this.passwordEncoder.matches("plainPassword", result.getPassword()));
    }

    @Test
    @DisplayName("register(): different passwords produce different hashes")
    void register_differentPasswordsProduceDifferentHashes() {
        UserRegistrationRequest request1 = new UserRegistrationRequest("user1", "user1@test.com", "password1");
        UserRegistrationRequest request2 = new UserRegistrationRequest("user2", "user2@test.com", "password1");

        when(this.userRepository.existsByUsername(anyString())).thenReturn(false);
        when(this.userRepository.existsByEmail(anyString())).thenReturn(false);
        when(this.userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result1 = this.authService.register(request1);
        User result2 = this.authService.register(request2);

        assertNotEquals(result1.getPassword(), result2.getPassword());
    }

    @Test
    @DisplayName("login(): successful login")
    void loginSuccess() {
        String hashedPassword = this.passwordEncoder.encode("password123");
        User user = new User("testuser", "test@test.com", hashedPassword);
        user.setUserId(1L);

        LoginRequest request = new LoginRequest("testuser", "password123");

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = this.authService.login(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("login(): null username throws exception")
    void login_nullUsername_throwsException() {
        LoginRequest request = new LoginRequest(null, "password");

        when(this.userRepository.findByUsername(null)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
    }

    @Test
    @DisplayName("login(): user not found throws exception")
    void login_userNotFound_throwsException() {
        LoginRequest request = new LoginRequest("nonexistent", "password123");

        when(this.userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
    }

    @Test
    @DisplayName("login(): incorrect password throws exception")
    void login_incorrectPassword_throwsException() {
        String hashedPassword = this.passwordEncoder.encode("correctPassword");
        User user = new User("testuser", "test@test.com", hashedPassword);

        LoginRequest request = new LoginRequest("testuser", "wrongPassword");

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
    }

    @Test
    @DisplayName("login(): empty password throws exception")
    void login_emptyPassword_throwsException() {
        String hashedPassword = this.passwordEncoder.encode("password123");
        User user = new User("testuser", "test@test.com", hashedPassword);

        LoginRequest request = new LoginRequest("testuser", "");

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
    }

    @Test
    @DisplayName("login(): username is case-sensitive")
    void login_usernameIsCaseSensitive() {
        String hashedPassword = this.passwordEncoder.encode("password123");
        User user = new User("TestUser", "test@test.com", hashedPassword);

        when(this.userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("testuser", "password123");

        assertThrows(UnauthorizedException.class, () -> this.authService.login(request));
    }

}
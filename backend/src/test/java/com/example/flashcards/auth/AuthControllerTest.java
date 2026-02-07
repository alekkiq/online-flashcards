package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.UnauthorizedException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;
import com.example.flashcards.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            SecurityConfig.class,
            JwtAuthFilter.class,
            JwtService.class,
            CustomUserDetailsService.class,
            CustomAuthenticationEntryPoint.class,
            CustomAccessDeniedHandler.class
        }
    )
)
@Import(TestSecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("AuthController test start");
    }

    @Test
    @DisplayName("register(): successfully creates new user")
    void register_success() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "newuser",
        "newuser@test.com",
        "password123"
        );

        User user = new User("newuser", "newuser@test.com", "hashedPassword");
        user.setUserId(1L);
        user.setRole(UserRole.STUDENT);

        when(this.authService.register(any(UserRegistrationRequest.class))).thenReturn(user);
        when(this.jwtService.generateToken(user)).thenReturn("mock.jwt.token");

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.userId").value(1))
            .andExpect(jsonPath("$.data.username").value("newuser"))
            .andExpect(jsonPath("$.data.email").value("newuser@test.com"))
            .andExpect(jsonPath("$.data.token").value("mock.jwt.token"));

        verify(this.authService, times(1)).register(any(UserRegistrationRequest.class));
        verify(this.jwtService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("register(): fails with duplicate username")
    void register_duplicateUsername_conflict() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "existinguser",
        "new@test.com",
        "password123"
        );

        when(this.authService.register(any(UserRegistrationRequest.class)))
            .thenThrow(new DuplicateResourceException("User", "Username already exists"));

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(this.authService, times(1)).register(any(UserRegistrationRequest.class));
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("register(): fails with duplicate email")
    void register_duplicateEmail_conflict() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "newuser",
        "existing@test.com",
        "password123"
        );

        when(this.authService.register(any(UserRegistrationRequest.class)))
            .thenThrow(new DuplicateResourceException("User", "Email already exists"));

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

        verify(this.authService, times(1)).register(any(UserRegistrationRequest.class));
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("register(): fails with invalid email format")
    void register_invalidEmail_badRequest() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "newuser",
        "invalid-email",
        "password123"
        );

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.authService, never()).register(any());
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("register(): fails with blank username")
    void register_blankUsername_badRequest() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "",
        "test@test.com",
        "password123"
        );

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.authService, never()).register(any());
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("register(): fails with blank password")
    void register_blankPassword_badRequest() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
        "newuser",
        "test@test.com",
        ""
        );

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.authService, never()).register(any());
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login(): successfully authenticates user")
    void login_success() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "password123");

        User user = new User("testuser", "test@test.com", "hashedPassword");
        user.setUserId(2L);
        user.setRole(UserRole.STUDENT);

        when(this.authService.login(any(LoginRequest.class))).thenReturn(user);
        when(this.jwtService.generateToken(user)).thenReturn("mock.jwt.token");

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.userId").value(2))
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.email").value("test@test.com"))
            .andExpect(jsonPath("$.data.token").value("mock.jwt.token"));

        verify(this.authService, times(1)).login(any(LoginRequest.class));
        verify(this.jwtService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("login(): fails with incorrect credentials")
    void login_incorrectCredentials_unauthorized() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");

        when(this.authService.login(any(LoginRequest.class)))
            .thenThrow(new UnauthorizedException("Invalid credentials"));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());

        verify(this.authService, times(1)).login(any(LoginRequest.class));
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login(): fails with non-existent user")
    void login_userNotFound_unauthorized() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent", "password123");

        when(this.authService.login(any(LoginRequest.class)))
            .thenThrow(new UnauthorizedException("User not found"));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());

        verify(this.authService, times(1)).login(any(LoginRequest.class));
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login(): fails with blank username")
    void login_blankUsername_badRequest() throws Exception {
        LoginRequest request = new LoginRequest("", "password123");

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.authService, never()).login(any());
        verify(this.jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login(): fails with blank password")
    void login_blankPassword_badRequest() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "");

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(this.authService, never()).login(any());
        verify(this.jwtService, never()).generateToken(any());
    }
}
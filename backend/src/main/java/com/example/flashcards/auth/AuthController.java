package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.AuthResponse;
import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.security.JwtService;
import com.example.flashcards.entity.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        User user = this.authService.register(request);
        String token = this.jwtService.generateToken(user);
        AuthResponse response = AuthResponse.from(user, token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Account created successfully."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        User user = this.authService.login(request);
        String token = this.jwtService.generateToken(user);
        AuthResponse response = AuthResponse.from(user, token);

        return ResponseEntity.ok(
            ApiResponse.success(response, "Welcome.")
        );
    }
}

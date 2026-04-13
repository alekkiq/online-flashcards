package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.AuthResponse;
import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public AuthController(AuthService authService, JwtService jwtService, MessageSource messageSource) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.messageSource = messageSource;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UserRegistrationRequest request, Locale locale) {
        User user = this.authService.register(request);
        String token = this.jwtService.generateToken(user);
        AuthResponse response = AuthResponse.from(user, token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, messageSource.getMessage("success.account.created", null, locale)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, Locale locale) {
        User user = this.authService.login(request);
        String token = this.jwtService.generateToken(user);
        AuthResponse response = AuthResponse.from(user, token);

        return ResponseEntity.ok(
                ApiResponse.success(response, messageSource.getMessage("success.login", null, locale))
        );
    }
}

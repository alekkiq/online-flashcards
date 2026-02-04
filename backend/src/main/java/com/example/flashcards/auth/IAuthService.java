package com.example.flashcards.auth;

import com.example.flashcards.auth.dto.AuthResponse;
import com.example.flashcards.auth.dto.LoginRequest;
import com.example.flashcards.auth.dto.UserRegistrationRequest;
import com.example.flashcards.user.User;

public interface IAuthService {
    User login(LoginRequest request);
    User register(UserRegistrationRequest request);
}

package com.example.flashcards.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotEmpty(message = "{validation.username.required}")
    String username,
    @NotEmpty(message = "{validation.password.required}")
    @Size(min = 6, message = "{validation.password.minLength}")
    String password
) {}

package com.example.flashcards.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
    @NotBlank(message = "validation.username.required")
    String username,
    @Email(message = "validation.email.invalid")
    @NotBlank(message = "validation.email.required")
    String email,
    @Size(min = 6, message = "validation.password.minLength")
    @NotBlank(message = "validation.password.required")
    String password
) {}

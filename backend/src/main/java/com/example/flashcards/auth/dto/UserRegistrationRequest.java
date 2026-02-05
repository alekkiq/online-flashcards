package com.example.flashcards.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
    @NotBlank String username,
    @Email @NotBlank String email,
    @Size(min = 6) @NotBlank String password
) {}

package com.example.flashcards.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailUpdateRequest(
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email
) {}

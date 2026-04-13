package com.example.flashcards.entity.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailUpdateRequest(
    @Email(message = "validation.email.invalid")
    @NotBlank(message = "validation.email.required")
    String email
) {}

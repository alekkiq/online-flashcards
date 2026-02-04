package com.example.flashcards.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailUpdateRequest(
    @Email @NotBlank String email
) {}

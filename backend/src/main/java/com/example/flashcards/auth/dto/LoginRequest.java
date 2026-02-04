package com.example.flashcards.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotEmpty String username,
    @NotEmpty @Size(min = 6) String password
) {}

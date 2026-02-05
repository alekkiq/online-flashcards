package com.example.flashcards.user.dto;

import com.example.flashcards.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
    @NotNull(message = "Role is required")
    UserRole role
) {}

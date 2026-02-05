package com.example.flashcards.entity.user.dto;

import com.example.flashcards.entity.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
    @NotNull(message = "Role is required")
    UserRole role
) {}

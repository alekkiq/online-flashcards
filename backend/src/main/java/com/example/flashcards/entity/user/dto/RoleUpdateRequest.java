package com.example.flashcards.entity.user.dto;

import com.example.flashcards.entity.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
    @NotNull(message = "{validation.role.required}")
    UserRole role
) {}

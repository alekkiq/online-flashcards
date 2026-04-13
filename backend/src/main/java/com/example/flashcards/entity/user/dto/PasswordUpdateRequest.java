package com.example.flashcards.entity.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
    @NotBlank(message = "validation.password.current.required")
    String oldPassword,

    @NotBlank(message = "validation.password.new.required")
    @Size(min = 6, message = "validation.password.new.minLength")
    String newPassword
) {}

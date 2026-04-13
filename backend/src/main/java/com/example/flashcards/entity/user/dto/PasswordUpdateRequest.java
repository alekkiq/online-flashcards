package com.example.flashcards.entity.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
    @NotBlank(message = "{validation.oldPassword.required}")
    String oldPassword,

    @NotBlank(message = "{validation.newPassword.required}")
    @Size(min = 6, message = "{validation.newPassword.minLength}")
    String newPassword
) {}

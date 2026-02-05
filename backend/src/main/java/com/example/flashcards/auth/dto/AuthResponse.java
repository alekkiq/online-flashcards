package com.example.flashcards.auth.dto;

import com.example.flashcards.entity.user.UserRole;
import com.example.flashcards.entity.user.User;

public record AuthResponse(
    String token,
    Long userId,
    String username,
    UserRole role
) {
    public static AuthResponse from(User user, String token) {
        return new AuthResponse(
            token,
            user.getUserId(),
            user.getUsername(),
            user.getRole()
        );
    }
}

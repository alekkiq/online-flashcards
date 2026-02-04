package com.example.flashcards.user.dto;

import com.example.flashcards.user.User;
import com.example.flashcards.user.UserRole;

public record UserResponse(
    Long userId,
    String username,
    String email,
    UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }
}

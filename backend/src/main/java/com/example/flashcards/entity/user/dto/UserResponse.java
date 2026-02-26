package com.example.flashcards.entity.user.dto;

import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;

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

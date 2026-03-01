package com.example.flashcards.entity.classroom.dto;

public record ClassroomUserResponse(
        long userId,
        String username,
        String role
) {}


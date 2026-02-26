package com.example.flashcards.entity.classroom.dto;

import com.example.flashcards.entity.classroom.Classroom;

public record ClassroomResponse(
        Long id,
        String title,
        String description,
        String note,
        String joinCode,
        String subjectName,
        String ownerUsername,
        boolean isOwner,
        int userCount
) {

    public static ClassroomResponse from(Classroom classroom, Long currentUserId) {
        // currentUserId voi olla null, siksi varmistus
        boolean isOwner = currentUserId != null
                && classroom.getOwner() != null
                && classroom.getOwner().getUserId() == currentUserId;

        return new ClassroomResponse(
                classroom.getClassroomId(),
                classroom.getTitle(),
                classroom.getDescription(),
                classroom.getNote(),
                classroom.getJoinCode(),
                classroom.getSubject().getName(),
                classroom.getOwner().getUsername(),
                isOwner,
                classroom.getUsers().size()
        );
    }
}
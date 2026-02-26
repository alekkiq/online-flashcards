package com.example.flashcards.entity.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassroomCreateRequest(
        @NotBlank(message = "Title is required.")
        @Size(max = 255, message = "Title must be at most 255 characters.")
        String title,

        @Size(max = 255, message = "Description must be at most 255 characters.")
        String description,

        @Size(max = 255, message = "Note must be at most 255 characters.")
        String note,

        @NotNull(message = "SubjectId is required.")
        Long subjectId
) {
}
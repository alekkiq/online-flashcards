package com.example.flashcards.entity.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassroomCreateRequest(
        @NotBlank(message = "validation.title.required")
        @Size(max = 255, message = "validation.title.maxLength")
        String title,

        @Size(max = 255, message = "validation.description.maxLength")
        String description,

        @Size(max = 255, message = "validation.note.maxLength")
        String note,

        String joinCode,

        @NotNull(message = "validation.subjectId.required")
        Long subjectId
) {
}

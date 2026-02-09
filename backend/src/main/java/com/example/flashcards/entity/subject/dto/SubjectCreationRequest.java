package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectCreationRequest(
    @NotBlank(message = "Subject name is required")
    String name
) {}

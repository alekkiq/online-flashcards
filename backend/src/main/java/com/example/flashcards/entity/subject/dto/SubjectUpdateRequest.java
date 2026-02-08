package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectUpdateRequest(
    @NotBlank(message = "New subject name is required")
    String name
) {}

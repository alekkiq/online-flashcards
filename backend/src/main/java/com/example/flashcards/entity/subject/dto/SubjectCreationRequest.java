package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectCreationRequest(
    @NotBlank(message = "Subject code is required")
    String code,

    @NotBlank(message = "Subject name is required")
    String name,

    @NotBlank(message = "Language is required")
    String language
) {}

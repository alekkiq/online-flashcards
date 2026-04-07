package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectUpdateRequest(
    @NotBlank(message = "Subject code is required")
    String code,

    @NotBlank(message = "New subject name is required")
    String name,

    @NotBlank(message = "Subject language is required")
    String language
) {}

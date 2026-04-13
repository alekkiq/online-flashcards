package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectUpdateRequest(
    @NotBlank(message = "validation.subject.code.required")
    String code,

    @NotBlank(message = "validation.subject.name.required")
    String name,

    @NotBlank(message = "validation.language.required")
    String language
) {}

package com.example.flashcards.entity.subject.dto;

import jakarta.validation.constraints.NotBlank;

public record SubjectCreationRequest(
    @NotBlank(message = "{validation.subjectCode.required}")
    String code,

    @NotBlank(message = "{validation.subjectName.required}")
    String name,

    @NotBlank(message = "{validation.language.required}")
    String language
) {}

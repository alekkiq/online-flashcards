package com.example.flashcards.entity.subject.dto;

import com.example.flashcards.entity.subject.Subject;

public record SubjectResponse(
    long subjectId,
    String code,
    String name,
    String language
) {
    public static SubjectResponse from(Subject subject) {
        return new SubjectResponse(
            subject.getSubjectId(),
            subject.getCode(),
            subject.getName(),
            subject.getLanguage()
        );
    }
}

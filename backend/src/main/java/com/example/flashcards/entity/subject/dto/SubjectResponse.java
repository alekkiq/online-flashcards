package com.example.flashcards.entity.subject.dto;

public record SubjectResponse(long subjectId, String name) {
    public static SubjectResponse from(long subjectId, String name) {
        return new SubjectResponse(subjectId, name);
    }
}

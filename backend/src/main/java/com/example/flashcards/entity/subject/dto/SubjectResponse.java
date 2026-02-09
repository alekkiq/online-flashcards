package com.example.flashcards.entity.subject.dto;

public record SubjectResponse(String name) {
    public static SubjectResponse from(String name) {
        return new SubjectResponse(name);
    }
}

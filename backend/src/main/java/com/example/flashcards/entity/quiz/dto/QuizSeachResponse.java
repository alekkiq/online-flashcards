package com.example.flashcards.entity.quiz.dto;

public record QuizSeachResponse(
    long quizId,
    String title,
    String description,
    String creatorUsername,
    String subjectName
) {}

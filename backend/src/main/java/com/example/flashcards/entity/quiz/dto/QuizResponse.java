package com.example.flashcards.entity.quiz.dto;

public record QuizResponse(
    long quizId,
    String title,
    String creatorUsername
) {}

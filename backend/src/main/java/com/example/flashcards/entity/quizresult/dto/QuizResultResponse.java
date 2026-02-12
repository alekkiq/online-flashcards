package com.example.flashcards.entity.quizresult.dto;

import java.time.LocalDateTime;

import com.example.flashcards.entity.quizresult.QuizResult;

public record QuizResultResponse(
    long quizResultId,
    long quizId,
    long userId,
    double scorePercentage,
    LocalDateTime completedAt
) {
    public static QuizResultResponse from(QuizResult result) {
    return new QuizResultResponse(
        result.getQuizResultId(),
        result.getQuiz().getQuizId(),
        result.getUser().getUserId(),
        result.getScorePercentage(),
        result.getTakenAt()
    );
}
}

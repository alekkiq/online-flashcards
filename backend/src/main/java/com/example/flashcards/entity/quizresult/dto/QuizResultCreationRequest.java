package com.example.flashcards.entity.quizresult.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record QuizResultCreationRequest(
    long quizId,
    @DecimalMin(value = "0.0", inclusive = true, message = "validation.score.range")
    @DecimalMax(value = "100.0", inclusive = true, message = "validation.score.range")
    double scorePercentage
) {
}

package com.example.flashcards.entity.quiz.dto;

import java.util.List;
import com.example.flashcards.entity.flashcard.dto.FlashcardResponse;

public record QuizResponse(
    long quizId,
    String title,
    String description,
    String creatorUsername,
    String subjectName,
    List<FlashcardResponse> flashcards
) {}

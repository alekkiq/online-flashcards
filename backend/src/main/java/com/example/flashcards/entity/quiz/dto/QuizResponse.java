package com.example.flashcards.entity.quiz.dto;

import java.util.List;
import com.example.flashcards.entity.flashcard.dto.FlashcardResponse;
import com.example.flashcards.entity.subject.Subject;

public record QuizResponse(
    long quizId,
    String title,
    String description,
    String language,
    String creatorUsername,
    String creatorRole,
    Subject subject,
    int cardCount,
    List<FlashcardResponse> flashcards
) {}

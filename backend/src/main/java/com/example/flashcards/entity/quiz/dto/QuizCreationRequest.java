package com.example.flashcards.entity.quiz.dto;

import java.util.List;
import com.example.flashcards.entity.flashcard.dto.FlashcardCreationRequest;

public record QuizCreationRequest(
    String title,
    String description,
    List<FlashcardCreationRequest> flashcards,
    String subject
) {}

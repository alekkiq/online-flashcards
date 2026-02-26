package com.example.flashcards.entity.flashcard.dto;

public record FlashcardCreationRequest(
    String question,
    String answer
) {
}

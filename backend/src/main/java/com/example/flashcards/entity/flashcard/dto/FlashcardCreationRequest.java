package com.example.flashcards.entity.flashcard.dto;

public record FlashcardCreationRequest(
    String front,
    String back
) {
}

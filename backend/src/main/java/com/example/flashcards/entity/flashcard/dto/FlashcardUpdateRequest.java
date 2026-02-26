package com.example.flashcards.entity.flashcard.dto;

public record FlashcardUpdateRequest(
    Long id, 
    String question,
    String answer
) {}
package com.example.flashcards.entity.flashcard.dto;

public record FlashcardResponse(
    long id, 
    String question, 
    String answer
) {}

package com.example.flashcards.entity.flashcard.dto;

public record FlashcardResponse(
    long id, 
    String front, 
    String back
) {}

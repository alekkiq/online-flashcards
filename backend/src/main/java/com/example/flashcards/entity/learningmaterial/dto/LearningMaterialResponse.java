package com.example.flashcards.entity.learningmaterial.dto;

public record LearningMaterialResponse(
        Long id,
        String title,
        String content,
        String creatorUsername
) {}

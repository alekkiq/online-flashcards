package com.example.flashcards.entity.classroom.dto;

import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialResponse;

import java.util.List;

public record ClassroomResponse(
        Long id,
        String title,
        String description,
        String note,
        String joinCode,
        String subjectName,
        String ownerUsername,
        boolean isOwner,
        int userCount,
        List<LearningMaterialResponse> learningMaterials
) {}
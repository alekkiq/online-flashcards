package com.example.flashcards.entity.classroom.dto;

import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;

import java.util.List;

public record ClassroomResponse(
        Long id,
        String title,
        String description,
        String note,
        String joinCode,
        String language,
        String subjectName,
        String ownerUsername,
        boolean isOwner,
        int userCount,
        List<ClassroomUserResponse> users,
        List<QuizSeachResponse> quizzes,
        List<LearningMaterialResponse> learningMaterials
) {}
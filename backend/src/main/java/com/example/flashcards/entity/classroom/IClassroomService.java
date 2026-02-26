package com.example.flashcards.entity.classroom;

import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;

import java.util.List;

public interface IClassroomService {
    List<Classroom> getMyClassrooms(Long userId);

    Classroom getClassroomById(Long classroomId);

    Classroom createClassroom(Long userId, ClassroomCreateRequest request);

    Classroom updateClassroom(Long userId, Long classroomId, ClassroomUpdateRequest request);

    Classroom joinByCode(Long userId, String joinCode);

    void leaveClassroom(Long userId, Long classroomId);

    Classroom addLearningMaterial(Long userId, Long classroomId, LearningMaterialCreationRequest request);

    Classroom removeLearningMaterial(Long userId, Long classroomId, Long learningMaterialId);
}
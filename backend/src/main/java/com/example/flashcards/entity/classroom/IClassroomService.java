package com.example.flashcards.entity.classroom;

import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;

import java.util.List;

public interface IClassroomService {
    List<Classroom> getMyClassrooms(Long userId);

    Classroom getClassroomById(Long classroomId);

    Classroom createClassroom(Long userId, ClassroomCreateRequest request);

    Classroom updateClassroom(Long userId, Long classroomId, ClassroomUpdateRequest request);

    Classroom joinByCode(Long userId, String joinCode);

    void leaveClassroom(Long userId, Long classroomId);
}
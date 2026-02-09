package com.example.flashcards.entity.subject;

import java.util.List;

public interface ISubjectService {
    List<Subject> getAllSubjects();
    Subject getSubjectByName(String name);
    Subject getSubjectById(Long subjectId);
    Subject createSubject(Subject subject);
    Subject updateSubject(Long subjectId, Subject subject);
    void deleteSubject(Long subjectId);
}

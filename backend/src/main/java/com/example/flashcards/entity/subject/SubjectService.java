package com.example.flashcards.entity.subject;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.subject.dto.SubjectResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements ISubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return this.subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(Long subjectId) {
        return this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", subjectId, "Subject with ID " + subjectId + " not found."));
    }

    @Override
    public Subject getSubjectByName(String name) {
        return this.subjectRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Subject with name '" + name + "' not found."));
    }

    @Override
    @Transactional
    public Subject createSubject(Subject subject) {
        if (this.subjectRepository.existsByName(subject.getName())) {
            throw new DuplicateResourceException("Subject", "A subject with name '" + subject.getName() + "' already exists.");
        }
        return this.subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public Subject updateSubject(Long subjectId, Subject subject) {
        Subject existingSubject = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", subjectId, "Subject with ID " + subjectId + " not found."));

        if (!existingSubject.getName().equals(subject.getName())
                && this.subjectRepository.existsByName(subject.getName())) {
            throw new DuplicateResourceException("Subject", "A subject with name '" + subject.getName() + "' already exists.");
        }

        // field updates
        existingSubject.setName(subject.getName());

        return this.subjectRepository.save(existingSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        if (!this.subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException("Subject", subjectId, "Subject with ID " + subjectId + " not found.");
        }
        this.subjectRepository.deleteById(subjectId);
    }
}

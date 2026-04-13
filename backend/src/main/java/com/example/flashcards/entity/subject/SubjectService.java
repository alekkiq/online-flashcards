package com.example.flashcards.entity.subject;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.common.provider.CurrentLanguageProvider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements ISubjectService {
    private static final String SUBJECT_RESOURCE = "Subject";

    private final SubjectRepository subjectRepository;
    private final CurrentLanguageProvider currentLanguageProvider;

    public SubjectService(
        SubjectRepository subjectRepository,
        CurrentLanguageProvider currentLanguageProvider
    ) {
        this.subjectRepository = subjectRepository;
        this.currentLanguageProvider = currentLanguageProvider;
    }

    @Override
    public List<Subject> getAllSubjects() {
        String language = currentLanguageProvider.getCurrentLanguage();
        return this.subjectRepository.findAllByLanguage(language);
    }

    @Override
    public Subject getSubjectById(Long subjectId) {
        return this.subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResourceNotFoundException(
                SUBJECT_RESOURCE,
                subjectId,
                "error.subject.notFound",
                new Object[]{subjectId}
            ));
    }

    @Override
    public Subject getSubjectByCode(String code) {
        String language = currentLanguageProvider.getCurrentLanguage();
        return this.subjectRepository.findByCodeAndLanguage(code, language)
            .orElseThrow(() -> new ResourceNotFoundException(
                SUBJECT_RESOURCE,
                "error.subject.nameNotFound",
                new Object[]{code}
            ));
    }

    @Override
    @Transactional
    public Subject createSubject(Subject subject) {
        if (this.subjectRepository.existsByCodeAndLanguage(subject.getCode(), subject.getLanguage())) {
            throw new DuplicateResourceException(
                SUBJECT_RESOURCE,
                "error.subject.duplicate",
                new Object[]{subject.getCode()}
            );
        }
        return this.subjectRepository.save(subject);
    }

    @Override
    @Transactional
    public Subject updateSubject(Long subjectId, Subject subject) {
        Subject existingSubject = this.subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResourceNotFoundException(
                SUBJECT_RESOURCE,
                subjectId,
                "error.subject.notFound",
                new Object[]{subjectId}
            ));

        boolean keyChanged =
            !existingSubject.getCode().equals(subject.getCode()) ||
            !existingSubject.getLanguage().equals(subject.getLanguage());

        if (keyChanged && this.subjectRepository.existsByCodeAndLanguage(subject.getCode(), subject.getLanguage())) {
            throw new DuplicateResourceException(
                SUBJECT_RESOURCE,
                "error.subject.duplicate",
                new Object[]{subject.getCode()}
            );
        }

        existingSubject.setCode(subject.getCode());
        existingSubject.setName(subject.getName());
        existingSubject.setLanguage(subject.getLanguage());

        return this.subjectRepository.save(existingSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long subjectId) {
        if (!this.subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException(
                SUBJECT_RESOURCE,
                subjectId,
                "error.subject.notFound",
                new Object[]{subjectId}
            );
        }
        this.subjectRepository.deleteById(subjectId);
    }
}

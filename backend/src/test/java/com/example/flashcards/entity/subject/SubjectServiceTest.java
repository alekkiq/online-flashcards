package com.example.flashcards.entity.subject;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.common.provider.CurrentLanguageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    private SubjectRepository subjectRepository;
    private SubjectService subjectService;

    private CurrentLanguageProvider currentLanguageProvider;

    @BeforeEach
    void setup() {
        this.subjectRepository = mock(SubjectRepository.class);
        this.currentLanguageProvider = mock(CurrentLanguageProvider.class);
        this.subjectService = new SubjectService(this.subjectRepository, this.currentLanguageProvider);

        when(this.currentLanguageProvider.getCurrentLanguage()).thenReturn("en");
    }

    @Test
    @DisplayName("getAllSubjects(): returns repository list for current language")
    void getAllSubjectsReturnsList() {
        Subject subject1 = new Subject("math", "Mathematics", "en");
        Subject subject2 = new Subject("physics", "Physics", "en");

        when(this.subjectRepository.findAllByLanguage("en")).thenReturn(List.of(subject1, subject2));

        List<Subject> result = this.subjectService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("math", result.get(0).getCode());
        assertEquals("physics", result.get(1).getCode());
        verify(this.subjectRepository, times(1)).findAllByLanguage("en");
    }

    @Test
    @DisplayName("getAllSubjects(): returns empty list when no subjects exist")
    void getAllSubjectsEmptyList() {
        when(this.subjectRepository.findAllByLanguage("en")).thenReturn(List.of());

        List<Subject> result = this.subjectService.getAllSubjects();

        assertTrue(result.isEmpty());
        verify(this.subjectRepository, times(1)).findAllByLanguage("en");
    }

    @Test
    @DisplayName("getSubjectById(): returns subject when found")
    void getSubjectByIdReturnsSubject() {
        Subject subject = new Subject("chemistry", "Chemistry", "en");
        subject.setSubjectId(5L);

        when(this.subjectRepository.findById(5L)).thenReturn(Optional.of(subject));

        Subject result = this.subjectService.getSubjectById(5L);

        assertEquals(5L, result.getSubjectId());
        assertEquals("chemistry", result.getCode());
        assertEquals("Chemistry", result.getName());
        assertEquals("en", result.getLanguage());
    }

    @Test
    @DisplayName("getSubjectById(): subject not found throws exception")
    void getSubjectByIdSubjectNotFoundThrowsException() {
        when(this.subjectRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.getSubjectById(123L));
    }

    @Test
    @DisplayName("getSubjectByCode(): returns subject for current language")
    void getSubjectByCodeReturnsSubject() {
        Subject subject = new Subject("biology", "Biology", "en");

        when(this.subjectRepository.findByCodeAndLanguage("biology", "en"))
                .thenReturn(Optional.of(subject));

        Subject result = this.subjectService.getSubjectByCode("biology");

        assertEquals("biology", result.getCode());
        assertEquals("Biology", result.getName());
        assertEquals("en", result.getLanguage());
        verify(this.subjectRepository, times(1)).findByCodeAndLanguage("biology", "en");
    }

    @Test
    @DisplayName("getSubjectByCode(): subject not found throws exception")
    void getSubjectByCodeSubjectNotFoundThrowsException() {
        when(this.subjectRepository.findByCodeAndLanguage("nonexistent", "en"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.getSubjectByCode("nonexistent"));
        verify(this.subjectRepository, times(1)).findByCodeAndLanguage("nonexistent", "en");
    }

    @Test
    @DisplayName("createSubject(): successful subject creation")
    void createSubjectSuccess() {
        Subject subject = new Subject("history", "History", "en");

        when(this.subjectRepository.existsByCodeAndLanguage("history", "en")).thenReturn(false);
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subject result = this.subjectService.createSubject(subject);

        assertEquals("history", result.getCode());
        assertEquals("History", result.getName());
        assertEquals("en", result.getLanguage());
        verify(this.subjectRepository, times(1)).save(subject);
    }

    @Test
    @DisplayName("createSubject(): duplicate code+language throws exception")
    void createSubjectDuplicateThrowsException() {
        Subject subject = new Subject("geography", "Geography", "en");

        when(this.subjectRepository.existsByCodeAndLanguage("geography", "en")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.subjectService.createSubject(subject));
        verify(this.subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateSubject(): successful subject update")
    void updateSubjectSuccess() {
        Subject existingSubject = new Subject("old-code", "Old Name", "en");
        existingSubject.setSubjectId(1L);

        Subject updatedSubject = new Subject("new-code", "New Name", "en");

        when(this.subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.existsByCodeAndLanguage("new-code", "en")).thenReturn(false);
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.subjectService.updateSubject(1L, updatedSubject);

        ArgumentCaptor<Subject> captor = ArgumentCaptor.forClass(Subject.class);
        verify(this.subjectRepository, times(1)).save(captor.capture());

        Subject saved = captor.getValue();
        assertEquals("new-code", saved.getCode());
        assertEquals("New Name", saved.getName());
        assertEquals("en", saved.getLanguage());
    }

    @Test
    @DisplayName("updateSubject(): same code/language allowed")
    void updateSubjectSameKeyAllowed() {
        Subject existingSubject = new Subject("english", "English", "en");
        existingSubject.setSubjectId(2L);

        Subject updatedSubject = new Subject("english", "English Updated", "en");

        when(this.subjectRepository.findById(2L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> this.subjectService.updateSubject(2L, updatedSubject));
        verify(this.subjectRepository, times(1)).save(any(Subject.class));
        verify(this.subjectRepository, never()).existsByCodeAndLanguage(anyString(), anyString());
    }

    @Test
    @DisplayName("updateSubject(): duplicate code+language throws exception")
    void updateSubjectDuplicateThrowsException() {
        Subject existingSubject = new Subject("art", "Art", "en");
        existingSubject.setSubjectId(3L);

        Subject updatedSubject = new Subject("music", "Music", "en");

        when(this.subjectRepository.findById(3L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.existsByCodeAndLanguage("music", "en")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.subjectService.updateSubject(3L, updatedSubject));
        verify(this.subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateSubject(): subject not found throws exception")
    void updateSubjectSubjectNotFoundThrowsException() {
        Subject updatedSubject = new Subject("new-subject", "New Subject", "en");

        when(this.subjectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.updateSubject(99L, updatedSubject));
        verify(this.subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteSubject(): successful deletion")
    void deleteSubjectSuccess() {
        when(this.subjectRepository.existsById(10L)).thenReturn(true);
        doNothing().when(this.subjectRepository).deleteById(10L);

        this.subjectService.deleteSubject(10L);

        verify(this.subjectRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("deleteSubject(): subject not found throws exception")
    void deleteSubjectSubjectNotFoundThrowsException() {
        when(this.subjectRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.deleteSubject(99L));
        verify(this.subjectRepository, never()).deleteById(any());
    }
}

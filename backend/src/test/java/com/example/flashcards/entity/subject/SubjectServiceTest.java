package com.example.flashcards.entity.subject;

import com.example.flashcards.common.exception.DuplicateResourceException;
import com.example.flashcards.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubjectServiceTest {
    private SubjectRepository subjectRepository;
    private SubjectService subjectService;

    @BeforeAll
    static void initAll() {
        System.out.println("SubjectService test start");
    }

    @BeforeEach
    void setup() {
        this.subjectRepository = Mockito.mock(SubjectRepository.class);
        this.subjectService = new SubjectService(this.subjectRepository);
    }

    @Test
    @DisplayName("getAllSubjects(): returns repository list")
    void getAllSubjectsReturnsList() {
        Subject subject1 = new Subject("Mathematics");
        Subject subject2 = new Subject("Physics");

        when(this.subjectRepository.findAll()).thenReturn(List.of(subject1, subject2));

        List<Subject> result = this.subjectService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("Mathematics", result.get(0).getName());
        assertEquals("Physics", result.get(1).getName());
    }

    @Test
    @DisplayName("getAllSubjects(): returns empty list when no subjects")
    void getAllSubjects_emptyList() {
        when(this.subjectRepository.findAll()).thenReturn(List.of());

        List<Subject> result = this.subjectService.getAllSubjects();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getSubjectById(): returns subject when found")
    void getSubjectByIdReturnsSubject() {
        Subject subject = new Subject("Chemistry");
        subject.setSubjectId(5L);

        when(this.subjectRepository.findById(5L)).thenReturn(Optional.of(subject));

        Subject result = this.subjectService.getSubjectById(5L);

        assertEquals(5L, result.getSubjectId());
        assertEquals("Chemistry", result.getName());
    }

    @Test
    @DisplayName("getSubjectById(): subject not found throws exception")
    void getSubjectById_subjectNotFound_throwsException() {
        when(this.subjectRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.getSubjectById(123L));
    }

    @Test
    @DisplayName("getSubjectByName(): returns subject when found")
    void getSubjectByNameReturnsSubject() {
        Subject subject = new Subject("Biology");

        when(this.subjectRepository.findByName("Biology")).thenReturn(Optional.of(subject));

        Subject result = this.subjectService.getSubjectByName("Biology");

        assertEquals("Biology", result.getName());
    }

    @Test
    @DisplayName("getSubjectByName(): subject not found throws exception")
    void getSubjectByName_subjectNotFound_throwsException() {
        when(this.subjectRepository.findByName("Nonexistent")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.getSubjectByName("Nonexistent"));
    }

    @Test
    @DisplayName("createSubject(): successful subject creation")
    void createSubjectSuccess() {
        Subject subject = new Subject("History");

        when(this.subjectRepository.existsByName("History")).thenReturn(false);
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));

        Subject result = this.subjectService.createSubject(subject);

        assertEquals("History", result.getName());
        verify(this.subjectRepository, times(1)).save(subject);
    }

    @Test
    @DisplayName("createSubject(): duplicate name throws exception")
    void createSubject_duplicateName_throwsException() {
        Subject subject = new Subject("Geography");

        when(this.subjectRepository.existsByName("Geography")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.subjectService.createSubject(subject));
        verify(this.subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateSubject(): successful subject update")
    void updateSubjectSuccess() {
        Subject existingSubject = new Subject("Old Name");
        existingSubject.setSubjectId(1L);

        Subject updatedSubject = new Subject("New Name");

        when(this.subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.existsByName("New Name")).thenReturn(false);
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));

        this.subjectService.updateSubject(1L, updatedSubject);

        ArgumentCaptor<Subject> captor = ArgumentCaptor.forClass(Subject.class);
        verify(this.subjectRepository, times(1)).save(captor.capture());
        assertEquals("New Name", captor.getValue().getName());
    }

    @Test
    @DisplayName("updateSubject(): same name allowed")
    void updateSubject_sameName_allowed() {
        Subject existingSubject = new Subject("English");
        existingSubject.setSubjectId(2L);

        Subject updatedSubject = new Subject("English");

        when(this.subjectRepository.findById(2L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> this.subjectService.updateSubject(2L, updatedSubject));
        verify(this.subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    @DisplayName("updateSubject(): duplicate name throws exception")
    void updateSubject_duplicateName_throwsException() {
        Subject existingSubject = new Subject("Art");
        existingSubject.setSubjectId(3L);

        Subject updatedSubject = new Subject("Music");

        when(this.subjectRepository.findById(3L)).thenReturn(Optional.of(existingSubject));
        when(this.subjectRepository.existsByName("Music")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> this.subjectService.updateSubject(3L, updatedSubject));
        verify(this.subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateSubject(): subject not found throws exception")
    void updateSubject_subjectNotFound_throwsException() {
        Subject updatedSubject = new Subject("NewSubject");

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
    void deleteSubject_subjectNotFound_throwsException() {
        when(this.subjectRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> this.subjectService.deleteSubject(99L));
        verify(this.subjectRepository, never()).deleteById(any());
    }
}
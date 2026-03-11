package com.example.flashcards.entity.classroom;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.classroom.dto.ClassroomCreateRequest;
import com.example.flashcards.entity.classroom.dto.ClassroomUpdateRequest;
import com.example.flashcards.entity.learningmaterial.LearningMaterial;
import com.example.flashcards.entity.learningmaterial.dto.LearningMaterialCreationRequest;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.quiz.QuizRepository;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.subject.SubjectRepository;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClassroomServiceTest {
    private ClassroomRepository classroomRepository;
    private UserRepository userRepository;
    private SubjectRepository subjectRepository;
    private QuizRepository quizRepository;
    private ClassroomService classroomService;

    @BeforeAll
    static void initAll() {
        System.out.println("ClassroomService test start");
    }

    @BeforeEach
    void setup() {
        this.classroomRepository = Mockito.mock(ClassroomRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.subjectRepository = Mockito.mock(SubjectRepository.class);
        this.quizRepository = Mockito.mock(QuizRepository.class);
        this.classroomService = new ClassroomService(
                this.classroomRepository, this.userRepository,
                this.subjectRepository, this.quizRepository
        );
    }

    @Test
    @DisplayName("getClassroomById(): returns classroom when found")
    void getClassroomById_returnsClassroom() {
        Subject subject = new Subject("Math");
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Classroom classroom = new Classroom("Title", "Desc", "Note", "ABC123", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findByClassroomIdAndUsers_UserId(1L, 1L)).thenReturn(Optional.of(classroom));

        Classroom result = this.classroomService.getClassroomById(1L, 1L);

        assertEquals(1L, result.getClassroomId());
        assertEquals("Title", result.getTitle());
    }

    @Test
    @DisplayName("getClassroomById(): not found throws exception")
    void getClassroomById_notFound_throwsException() {
        when(this.classroomRepository.findByClassroomIdAndUsers_UserId(eq(99L), anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.getClassroomById(99L, 1L));
    }

    @Test
    @DisplayName("getMyClassrooms(): returns list of classrooms")
    void getMyClassrooms_returnsList() {
        Subject subject = new Subject("Math");
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Classroom c1 = new Classroom("Class 1", null, null, "CODE01", owner, subject);
        Classroom c2 = new Classroom("Class 2", null, null, "CODE02", owner, subject);

        when(this.classroomRepository.findByUsers_UserId(1L)).thenReturn(List.of(c1, c2));

        List<Classroom> result = this.classroomService.getMyClassrooms(1L);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getMyClassrooms(): returns empty list")
    void getMyClassrooms_emptyList() {
        when(this.classroomRepository.findByUsers_UserId(1L)).thenReturn(List.of());

        List<Classroom> result = this.classroomService.getMyClassrooms(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("createClassroom(): generates join code when not provided")
    void createClassroom_generatesJoinCode() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        subject.setSubjectId(10L);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", null, 10L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.subjectRepository.findById(10L)).thenReturn(Optional.of(subject));
        when(this.classroomRepository.existsByJoinCode(anyString())).thenReturn(false);
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.createClassroom(1L, request);

        assertNotNull(result.getJoinCode());
        assertEquals(6, result.getJoinCode().length());
        assertEquals("Title", result.getTitle());
        assertTrue(result.getUsers().contains(owner));
        verify(this.classroomRepository, times(1)).save(any(Classroom.class));
    }

    @Test
    @DisplayName("createClassroom(): uses custom join code when provided")
    void createClassroom_withCustomJoinCode() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        subject.setSubjectId(10L);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", "MYCODE", 10L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.subjectRepository.findById(10L)).thenReturn(Optional.of(subject));
        when(this.classroomRepository.existsByJoinCode("MYCODE")).thenReturn(false);
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.createClassroom(1L, request);

        assertEquals("MYCODE", result.getJoinCode());
    }

    @Test
    @DisplayName("createClassroom(): custom join code too short throws exception")
    void createClassroom_joinCodeTooShort_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        subject.setSubjectId(10L);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", "AB", 10L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.subjectRepository.findById(10L)).thenReturn(Optional.of(subject));

        assertThrows(IllegalArgumentException.class, () -> this.classroomService.createClassroom(1L, request));
    }

    @Test
    @DisplayName("createClassroom(): duplicate join code throws exception")
    void createClassroom_duplicateJoinCode_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        subject.setSubjectId(10L);

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", "TAKEN1", 10L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.subjectRepository.findById(10L)).thenReturn(Optional.of(subject));
        when(this.classroomRepository.existsByJoinCode("TAKEN1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> this.classroomService.createClassroom(1L, request));
    }

    @Test
    @DisplayName("createClassroom(): user not found throws exception")
    void createClassroom_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", null, 10L);
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.createClassroom(99L, request));
    }

    @Test
    @DisplayName("createClassroom(): subject not found throws exception")
    void createClassroom_subjectNotFound_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.subjectRepository.findById(99L)).thenReturn(Optional.empty());

        ClassroomCreateRequest request = new ClassroomCreateRequest("Title", "Desc", "Note", null, 99L);
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.createClassroom(1L, request));
    }

    @Test
    @DisplayName("updateClassroom(): owner updates successfully")
    void updateClassroom_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Old Title", "Old Desc", "Old Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        ClassroomUpdateRequest request = new ClassroomUpdateRequest("New Title", "New Desc", "New Note");

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.updateClassroom(1L, 1L, request);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals("New Note", result.getNote());
    }

    @Test
    @DisplayName("updateClassroom(): non-owner throws exception")
    void updateClassroom_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        ClassroomUpdateRequest request = new ClassroomUpdateRequest("New Title", "New Desc", "New Note");

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class, () -> this.classroomService.updateClassroom(99L, 1L, request));
    }

    @Test
    @DisplayName("updateClassroom(): classroom not found throws exception")
    void updateClassroom_classroomNotFound_throwsException() {
        when(this.classroomRepository.findById(99L)).thenReturn(Optional.empty());

        ClassroomUpdateRequest request = new ClassroomUpdateRequest("Title", "Desc", "Note");
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.updateClassroom(1L, 99L, request));
    }

    @Test
    @DisplayName("joinByCode(): user joins classroom successfully")
    void joinByCode_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        User newUser = new User("student", "student@test.com", "password");
        newUser.setUserId(2L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "JOIN01", owner, subject);
        classroom.setClassroomId(1L);
        classroom.addUser(owner);

        when(this.userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(this.classroomRepository.findByJoinCode("JOIN01")).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.joinByCode(2L, "JOIN01");

        assertTrue(result.getUsers().contains(newUser));
        verify(this.classroomRepository, times(1)).save(any(Classroom.class));
    }

    @Test
    @DisplayName("joinByCode(): already a member returns without duplicate")
    void joinByCode_alreadyMember() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "JOIN01", owner, subject);
        classroom.setClassroomId(1L);
        classroom.addUser(owner);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.classroomRepository.findByJoinCode("JOIN01")).thenReturn(Optional.of(classroom));

        Classroom result = this.classroomService.joinByCode(1L, "JOIN01");

        assertEquals(1, result.getUsers().size());
        verify(this.classroomRepository, never()).save(any(Classroom.class));
    }

    @Test
    @DisplayName("joinByCode(): invalid code throws exception")
    void joinByCode_invalidCode_throwsException() {
        User user = new User("student", "student@test.com", "password");
        user.setUserId(1L);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.classroomRepository.findByJoinCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.joinByCode(1L, "INVALID"));
    }

    @Test
    @DisplayName("joinByCode(): user not found throws exception")
    void joinByCode_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.joinByCode(99L, "JOIN01"));
    }

    @Test
    @DisplayName("leaveClassroom(): non-owner leaves successfully")
    void leaveClassroom_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        User student = new User("student", "student@test.com", "password");
        student.setUserId(2L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);
        classroom.addUser(owner);
        classroom.addUser(student);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        this.classroomService.leaveClassroom(2L, 1L);

        assertFalse(classroom.getUsers().stream().anyMatch(u -> u.getUserId() == 2L));
        verify(this.classroomRepository, times(1)).save(any(Classroom.class));
    }

    @Test
    @DisplayName("leaveClassroom(): owner cannot leave throws exception")
    void leaveClassroom_ownerCannotLeave_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class, () -> this.classroomService.leaveClassroom(1L, 1L));
    }

    @Test
    @DisplayName("leaveClassroom(): classroom not found throws exception")
    void leaveClassroom_classroomNotFound_throwsException() {
        when(this.classroomRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.classroomService.leaveClassroom(1L, 99L));
    }

    @Test
    @DisplayName("removeUserFromClassroom(): owner removes user successfully")
    void removeUserFromClassroom_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        User student = new User("student", "student@test.com", "password");
        student.setUserId(2L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);
        classroom.addUser(owner);
        classroom.addUser(student);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.removeUserFromClassroom(1L, 1L, 2L);

        assertFalse(result.getUsers().stream().anyMatch(u -> u.getUserId() == 2L));
    }

    @Test
    @DisplayName("removeUserFromClassroom(): non-owner throws exception")
    void removeUserFromClassroom_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class,
                () -> this.classroomService.removeUserFromClassroom(99L, 1L, 2L));
    }

    @Test
    @DisplayName("removeUserFromClassroom(): owner cannot remove self throws exception")
    void removeUserFromClassroom_ownerCannotRemoveSelf_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class,
                () -> this.classroomService.removeUserFromClassroom(1L, 1L, 1L));
    }

    @Test
    @DisplayName("addLearningMaterial(): owner adds material successfully")
    void addLearningMaterial_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        LearningMaterialCreationRequest request = new LearningMaterialCreationRequest("Material Title", "Content");

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.addLearningMaterial(1L, 1L, request);

        assertEquals(1, result.getLearningMaterials().size());
        assertEquals("Material Title", result.getLearningMaterials().get(0).getTitle());
    }

    @Test
    @DisplayName("addLearningMaterial(): non-owner throws exception")
    void addLearningMaterial_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.userRepository.findById(99L)).thenReturn(Optional.of(new User("other", "o@t.com", "p")));
        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        LearningMaterialCreationRequest request = new LearningMaterialCreationRequest("Title", "Content");
        assertThrows(IllegalArgumentException.class, () -> this.classroomService.addLearningMaterial(99L, 1L, request));
    }

    @Test
    @DisplayName("removeLearningMaterial(): owner removes material successfully")
    void removeLearningMaterial_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);
        LearningMaterial material = new LearningMaterial("Material", "Content", classroom, owner);
        classroom.addLearningMaterial(material);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.removeLearningMaterial(1L, 1L, 0L);

        assertTrue(result.getLearningMaterials().isEmpty());
    }

    @Test
    @DisplayName("removeLearningMaterial(): non-owner throws exception")
    void removeLearningMaterial_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class, () -> this.classroomService.removeLearningMaterial(99L, 1L, 1L));
    }

    @Test
    @DisplayName("addQuizToClassroom(): owner adds quiz successfully")
    void addQuizToClassroom_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        Quiz quiz = new Quiz("Quiz 1", "Desc", owner, subject);
        quiz.setQuizId(10L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.addQuizToClassroom(1L, 1L, 10L);

        assertEquals(1, result.getQuizzes().size());
    }

    @Test
    @DisplayName("addQuizToClassroom(): already added returns without duplicate")
    void addQuizToClassroom_alreadyAdded() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        Quiz quiz = new Quiz("Quiz 1", "Desc", owner, subject);
        quiz.setQuizId(10L);
        classroom.addQuiz(quiz);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        Classroom result = this.classroomService.addQuizToClassroom(1L, 1L, 10L);

        assertEquals(1, result.getQuizzes().size());
        verify(this.classroomRepository, never()).save(any(Classroom.class));
    }

    @Test
    @DisplayName("addQuizToClassroom(): non-owner throws exception")
    void addQuizToClassroom_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class,
                () -> this.classroomService.addQuizToClassroom(99L, 1L, 10L));
    }

    @Test
    @DisplayName("addQuizToClassroom(): quiz not found throws exception")
    void addQuizToClassroom_quizNotFound_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.quizRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.classroomService.addQuizToClassroom(1L, 1L, 99L));
    }

    @Test
    @DisplayName("removeQuizFromClassroom(): owner removes quiz successfully")
    void removeQuizFromClassroom_success() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        Quiz quiz = new Quiz("Quiz 1", "Desc", owner, subject);
        quiz.setQuizId(10L);
        classroom.addQuiz(quiz);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
        when(this.classroomRepository.save(any(Classroom.class))).thenAnswer(i -> i.getArgument(0));

        Classroom result = this.classroomService.removeQuizFromClassroom(1L, 1L, 10L);

        assertTrue(result.getQuizzes().isEmpty());
    }

    @Test
    @DisplayName("removeQuizFromClassroom(): non-owner throws exception")
    void removeQuizFromClassroom_notOwner_throwsException() {
        User owner = new User("teacher", "teacher@test.com", "password");
        owner.setUserId(1L);
        Subject subject = new Subject("Math");
        Classroom classroom = new Classroom("Title", "Desc", "Note", "CODE01", owner, subject);
        classroom.setClassroomId(1L);

        when(this.classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        assertThrows(IllegalArgumentException.class,
                () -> this.classroomService.removeQuizFromClassroom(99L, 1L, 10L));
    }
}


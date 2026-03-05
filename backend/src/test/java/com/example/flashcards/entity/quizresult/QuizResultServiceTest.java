package com.example.flashcards.entity.quizresult;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.quiz.QuizRepository;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import com.example.flashcards.entity.user.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuizResultServiceTest {
    private QuizResultRepository quizResultRepository;
    private UserRepository userRepository;
    private QuizRepository quizRepository;
    private QuizResultService quizResultService;

    @BeforeAll
    static void initAll() {
        System.out.println("QuizResultService test start");
    }

    @BeforeEach
    void setup() {
        this.quizResultRepository = Mockito.mock(QuizResultRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.quizRepository = Mockito.mock(QuizRepository.class);
        this.quizResultService = new QuizResultService(this.quizResultRepository, this.userRepository, this.quizRepository);
    }

    private User createTestUser() {
        User user = new User("student", "student@test.com", "password");
        user.setUserId(1L);
        user.setRole(UserRole.STUDENT);
        return user;
    }

    private Quiz createTestQuiz() {
        User creator = new User("teacher", "teacher@test.com", "password");
        creator.setUserId(2L);
        Subject subject = new Subject("Math");
        Quiz quiz = new Quiz("Quiz Title", "Desc", creator, subject);
        quiz.setQuizId(10L);
        return quiz;
    }

    // --- createQuizResult ---

    @Test
    @DisplayName("createQuizResult(): successfully creates quiz result")
    void createQuizResult_success() {
        User user = createTestUser();
        Quiz quiz = createTestQuiz();

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(this.quizResultRepository.save(any(QuizResult.class))).thenAnswer(i -> i.getArgument(0));

        QuizResult result = this.quizResultService.createQuizResult(1L, 10L, 85.5);

        assertEquals(85.5, result.getScorePercentage());
        assertEquals(user, result.getUser());
        assertEquals(quiz, result.getQuiz());
        verify(this.quizResultRepository, times(1)).save(any(QuizResult.class));
    }

    @Test
    @DisplayName("createQuizResult(): user not found throws exception")
    void createQuizResult_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.quizResultService.createQuizResult(99L, 10L, 85.5));
        verify(this.quizResultRepository, never()).save(any());
    }

    @Test
    @DisplayName("createQuizResult(): quiz not found throws exception")
    void createQuizResult_quizNotFound_throwsException() {
        User user = createTestUser();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.quizResultService.createQuizResult(1L, 99L, 85.5));
        verify(this.quizResultRepository, never()).save(any());
    }

    // --- getQuizResultByQuizAndUser ---

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): returns list of results")
    void getQuizResultByQuizAndUser_success() {
        User user = createTestUser();
        Quiz quiz = createTestQuiz();

        QuizResult r1 = new QuizResult(80.0, null, user, quiz);
        QuizResult r2 = new QuizResult(90.0, null, user, quiz);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizRepository.findById(10L)).thenReturn(Optional.of(quiz));
        when(this.quizResultRepository.findByQuizAndUser(quiz, user)).thenReturn(List.of(r1, r2));

        List<QuizResult> result = this.quizResultService.getQuizResultByQuizAndUser(1L, 10L);

        assertEquals(2, result.size());
        assertEquals(80.0, result.get(0).getScorePercentage());
        assertEquals(90.0, result.get(1).getScorePercentage());
    }

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): user not found throws exception")
    void getQuizResultByQuizAndUser_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.quizResultService.getQuizResultByQuizAndUser(99L, 10L));
    }

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): quiz not found throws exception")
    void getQuizResultByQuizAndUser_quizNotFound_throwsException() {
        User user = createTestUser();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.quizResultService.getQuizResultByQuizAndUser(1L, 99L));
    }

    // --- getQuizResultByUser ---

    @Test
    @DisplayName("getQuizResultByUser(): returns list of results")
    void getQuizResultByUser_success() {
        User user = createTestUser();
        Quiz quiz = createTestQuiz();

        QuizResult r1 = new QuizResult(75.0, null, user, quiz);
        QuizResult r2 = new QuizResult(95.0, null, user, quiz);

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizResultRepository.findByUser(user)).thenReturn(List.of(r1, r2));

        List<QuizResult> result = this.quizResultService.getQuizResultByUser(1L);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getQuizResultByUser(): returns empty list when no results")
    void getQuizResultByUser_emptyList() {
        User user = createTestUser();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.quizResultRepository.findByUser(user)).thenReturn(List.of());

        List<QuizResult> result = this.quizResultService.getQuizResultByUser(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getQuizResultByUser(): user not found throws exception")
    void getQuizResultByUser_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> this.quizResultService.getQuizResultByUser(99L));
    }
}


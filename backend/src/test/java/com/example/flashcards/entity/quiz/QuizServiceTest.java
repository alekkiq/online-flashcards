package com.example.flashcards.entity.quiz;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.flashcard.Flashcard;
import com.example.flashcards.entity.flashcard.dto.FlashcardCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.subject.SubjectRepository;
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

class QuizServiceTest {
    private QuizRepository quizRepository;
    private UserRepository userRepository;
    private SubjectRepository subjectRepository;
    private QuizService quizService;

    @BeforeAll
    static void initAll() {
        System.out.println("QuizService test start");
    }

    @BeforeEach
    void setup() {
        this.quizRepository = Mockito.mock(QuizRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.subjectRepository = Mockito.mock(SubjectRepository.class);
        this.quizService = new QuizService(this.quizRepository, this.userRepository, this.subjectRepository);
    }

    private User createTestUser() {
        User user = new User("teacher", "teacher@test.com", "password");
        user.setUserId(1L);
        user.setRole(UserRole.TEACHER);
        return user;
    }

    private Subject createTestSubject() {
        Subject subject = new Subject("Math");
        subject.setSubjectId(10L);
        return subject;
    }

    private Quiz createTestQuiz(User creator, Subject subject) {
        Quiz quiz = new Quiz("Quiz Title", "Quiz Desc", creator, subject);
        quiz.setQuizId(1L);
        Flashcard fc = new Flashcard("Q1?", "A1", quiz);
        fc.setFlashcardId(1L);
        quiz.addFlashcard(fc);
        return quiz;
    }

    // --- getQuizById ---

    @Test
    @DisplayName("getQuizById(): returns quiz response when found")
    void getQuizById_success() {
        User creator = createTestUser();
        Subject subject = createTestSubject();
        Quiz quiz = createTestQuiz(creator, subject);

        when(this.quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

        QuizResponse result = this.quizService.getQuizById(1L);

        assertEquals(1L, result.quizId());
        assertEquals("Quiz Title", result.title());
        assertEquals("teacher", result.creatorUsername());
        assertEquals("Math", result.subjectName());
        assertEquals(1, result.cardCount());
        assertEquals(1, result.flashcards().size());
    }

    @Test
    @DisplayName("getQuizById(): not found throws exception")
    void getQuizById_notFound_throwsException() {
        when(this.quizRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.getQuizById(99L));
    }

    // --- searchQuizzes ---

    @Test
    @DisplayName("searchQuizzes(): returns list of quiz search responses")
    void searchQuizzes_returnsList() {
        User creator = createTestUser();
        Subject subject = createTestSubject();
        Quiz quiz1 = createTestQuiz(creator, subject);
        Quiz quiz2 = new Quiz("Quiz 2", "Desc 2", creator, subject);
        quiz2.setQuizId(2L);

        when(this.quizRepository.getAllQuizzes()).thenReturn(List.of(quiz1, quiz2));

        List<QuizSeachResponse> result = this.quizService.searchQuizzes();

        assertEquals(2, result.size());
        assertEquals("Quiz Title", result.get(0).title());
        assertEquals("Quiz 2", result.get(1).title());
    }

    @Test
    @DisplayName("searchQuizzes(): returns empty list when no quizzes")
    void searchQuizzes_emptyList() {
        when(this.quizRepository.getAllQuizzes()).thenReturn(List.of());

        List<QuizSeachResponse> result = this.quizService.searchQuizzes();

        assertTrue(result.isEmpty());
    }

    // --- createQuiz ---

    @Test
    @DisplayName("createQuiz(): successfully creates quiz with flashcards")
    void createQuiz_withFlashcards_success() {
        User creator = createTestUser();
        Subject subject = createTestSubject();

        List<FlashcardCreationRequest> flashcards = List.of(
            new FlashcardCreationRequest("Q1?", "A1"),
            new FlashcardCreationRequest("Q2?", "A2")
        );
        QuizCreationRequest request = new QuizCreationRequest("New Quiz", "Desc", flashcards, "Math");

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(this.subjectRepository.findByName("Math")).thenReturn(Optional.of(subject));
        when(this.quizRepository.save(any(Quiz.class))).thenAnswer(i -> {
            Quiz saved = i.getArgument(0);
            saved.setQuizId(1L);
            return saved;
        });

        QuizResponse result = this.quizService.createQuiz(1L, request);

        assertEquals("New Quiz", result.title());
        assertEquals(2, result.cardCount());
        verify(this.quizRepository, times(1)).save(any(Quiz.class));
    }

    @Test
    @DisplayName("createQuiz(): successfully creates quiz without flashcards")
    void createQuiz_withoutFlashcards_success() {
        User creator = createTestUser();
        Subject subject = createTestSubject();

        QuizCreationRequest request = new QuizCreationRequest("No Cards Quiz", "Desc", null, "Math");

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(this.subjectRepository.findByName("Math")).thenReturn(Optional.of(subject));
        when(this.quizRepository.save(any(Quiz.class))).thenAnswer(i -> {
            Quiz saved = i.getArgument(0);
            saved.setQuizId(1L);
            return saved;
        });

        QuizResponse result = this.quizService.createQuiz(1L, request);

        assertEquals("No Cards Quiz", result.title());
        assertEquals(0, result.cardCount());
    }

    @Test
    @DisplayName("createQuiz(): user not found throws exception")
    void createQuiz_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", null, "Math");
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.createQuiz(99L, request));
    }

    @Test
    @DisplayName("createQuiz(): subject not found throws exception")
    void createQuiz_subjectNotFound_throwsException() {
        User creator = createTestUser();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(this.subjectRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", null, "Nonexistent");
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.createQuiz(1L, request));
    }

    // --- updateQuiz ---

    @Test
    @DisplayName("updateQuiz(): successfully updates quiz")
    void updateQuiz_success() {
        User creator = createTestUser();
        Subject subject = createTestSubject();
        Quiz existingQuiz = createTestQuiz(creator, subject);

        Subject newSubject = new Subject("Physics");
        newSubject.setSubjectId(20L);

        List<FlashcardCreationRequest> newFlashcards = List.of(
            new FlashcardCreationRequest("New Q?", "New A")
        );
        QuizCreationRequest request = new QuizCreationRequest("Updated Quiz", "New Desc", newFlashcards, "Physics");

        when(this.quizRepository.findById(1L)).thenReturn(Optional.of(existingQuiz));
        when(this.subjectRepository.findByName("Physics")).thenReturn(Optional.of(newSubject));
        when(this.quizRepository.save(any(Quiz.class))).thenAnswer(i -> i.getArgument(0));

        QuizResponse result = this.quizService.updateQuiz(1L, 1L, request);

        assertEquals("Updated Quiz", result.title());
        assertEquals("New Desc", result.description());
        assertEquals("Physics", result.subjectName());
    }

    @Test
    @DisplayName("updateQuiz(): quiz not found throws exception")
    void updateQuiz_quizNotFound_throwsException() {
        when(this.quizRepository.findById(99L)).thenReturn(Optional.empty());

        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", null, "Math");
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.updateQuiz(99L, 1L, request));
    }

    @Test
    @DisplayName("updateQuiz(): subject not found throws exception")
    void updateQuiz_subjectNotFound_throwsException() {
        User creator = createTestUser();
        Subject subject = createTestSubject();
        Quiz existingQuiz = createTestQuiz(creator, subject);

        when(this.quizRepository.findById(1L)).thenReturn(Optional.of(existingQuiz));
        when(this.subjectRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", null, "Nonexistent");
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.updateQuiz(1L, 1L, request));
    }

    // --- deleteQuiz ---

    @Test
    @DisplayName("deleteQuiz(): successfully deletes quiz")
    void deleteQuiz_success() {
        when(this.quizRepository.existsById(1L)).thenReturn(true);

        this.quizService.deleteQuiz(1L);

        verify(this.quizRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteQuiz(): not found throws exception")
    void deleteQuiz_notFound_throwsException() {
        when(this.quizRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> this.quizService.deleteQuiz(99L));
    }

    // --- getQuizzesByUser ---

    @Test
    @DisplayName("getQuizzesByUser(): returns list of user's quizzes")
    void getQuizzesByUser_returnsList() {
        User creator = createTestUser();
        Subject subject = createTestSubject();
        Quiz quiz1 = createTestQuiz(creator, subject);
        Quiz quiz2 = new Quiz("Quiz 2", "Desc 2", creator, subject);
        quiz2.setQuizId(2L);

        when(this.quizRepository.findByCreator_UserId(1L)).thenReturn(List.of(quiz1, quiz2));

        List<QuizResponse> result = this.quizService.getQuizzesByUser(1L);

        assertEquals(2, result.size());
        assertEquals("Quiz Title", result.get(0).title());
        assertEquals("Quiz 2", result.get(1).title());
    }
}


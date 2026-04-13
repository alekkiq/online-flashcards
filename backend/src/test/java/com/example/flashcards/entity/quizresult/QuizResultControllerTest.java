package com.example.flashcards.entity.quizresult;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.quizresult.dto.QuizResultCreationRequest;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRole;
import com.example.flashcards.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = QuizResultController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            SecurityConfig.class,
            JwtAuthFilter.class,
            JwtService.class,
            CustomUserDetailsService.class,
            CustomAuthenticationEntryPoint.class,
            CustomAccessDeniedHandler.class
        }
    )
)
@Import(TestSecurityConfig.class)
class QuizResultControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizResultService quizResultService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("QuizResultController test start");
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
        Subject subject = new Subject("math", "Mathematics", "en");
        Quiz quiz = new Quiz("Quiz Title", "Desc", "en", creator, subject);
        quiz.setQuizId(10L);
        return quiz;
    }

    private QuizResult createTestQuizResult(User user, Quiz quiz, double score) {
        QuizResult result = new QuizResult(score, LocalDateTime.now(), user, quiz);
        return result;
    }

    // --- POST /api/v1/quiz-results ---

    @Test
    @DisplayName("createQuizResult(): successfully creates quiz result")
    void createQuizResult_success() throws Exception {
        User user = createTestUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Quiz quiz = createTestQuiz();

        QuizResult quizResult = createTestQuizResult(user, quiz, 85.5);
        QuizResultCreationRequest request = new QuizResultCreationRequest(10L, 85.5);

        when(this.quizResultService.createQuizResult(1L, 10L, 85.5)).thenReturn(quizResult);

        this.mockMvc.perform(post("/api/v1/quiz-results")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz result saved successfully."))
            .andExpect(jsonPath("$.data.scorePercentage").value(85.5));

        verify(this.quizResultService, times(1)).createQuizResult(1L, 10L, 85.5);
    }

    @Test
    @DisplayName("createQuizResult(): unauthenticated returns forbidden")
    void createQuizResult_unauthenticated_forbidden() throws Exception {
        QuizResultCreationRequest request = new QuizResultCreationRequest(10L, 85.5);

        this.mockMvc.perform(post("/api/v1/quiz-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("createQuizResult(): invalid score returns bad request")
    void createQuizResult_invalidScore_badRequest() throws Exception {
        User user = createTestUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        QuizResultCreationRequest request = new QuizResultCreationRequest(10L, 150.0);

        this.mockMvc.perform(post("/api/v1/quiz-results")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // --- GET /api/v1/quiz-results/me/quiz/{quizId} ---

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): returns list of results")
    void getQuizResultByQuizAndUser_success() throws Exception {
        User user = createTestUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Quiz quiz = createTestQuiz();

        QuizResult r1 = createTestQuizResult(user, quiz, 80.0);
        QuizResult r2 = createTestQuizResult(user, quiz, 90.0);

        when(this.quizResultService.getQuizResultByQuizAndUser(1L, 10L)).thenReturn(List.of(r1, r2));

        this.mockMvc.perform(get("/api/v1/quiz-results/me/quiz/10")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz results fetched successfully."))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));

        verify(this.quizResultService, times(1)).getQuizResultByQuizAndUser(1L, 10L);
    }

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): quiz not found returns 404")
    void getQuizResultByQuizAndUser_quizNotFound_returns404() throws Exception {
        User user = createTestUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(this.quizResultService.getQuizResultByQuizAndUser(1L, 99L))
            .thenThrow(new ResourceNotFoundException("Quiz", "Quiz with ID 99 not found."));

        this.mockMvc.perform(get("/api/v1/quiz-results/me/quiz/99")
                .with(user(userDetails)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getQuizResultByQuizAndUser(): unauthenticated returns forbidden")
    void getQuizResultByQuizAndUser_unauthenticated_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/quiz-results/me/quiz/10"))
            .andExpect(status().isForbidden());
    }

    // --- GET /api/v1/quiz-results/me ---

    @Test
    @DisplayName("getQuizResultByUser(): returns user's results")
    void getQuizResultByUser_success() throws Exception {
        User user = createTestUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Quiz quiz = createTestQuiz();

        QuizResult r1 = createTestQuizResult(user, quiz, 75.0);
        QuizResult r2 = createTestQuizResult(user, quiz, 95.0);

        when(this.quizResultService.getQuizResultByUser(1L)).thenReturn(List.of(r1, r2));

        this.mockMvc.perform(get("/api/v1/quiz-results/me")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz results fetched succesfully."))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));

        verify(this.quizResultService, times(1)).getQuizResultByUser(1L);
    }

    @Test
    @DisplayName("getQuizResultByUser(): unauthenticated returns forbidden")
    void getQuizResultByUser_unauthenticated_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/quiz-results/me"))
            .andExpect(status().isForbidden());
    }
}


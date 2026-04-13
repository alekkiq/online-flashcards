package com.example.flashcards.entity.quiz;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.flashcard.dto.FlashcardCreationRequest;
import com.example.flashcards.entity.flashcard.dto.FlashcardResponse;
import com.example.flashcards.entity.quiz.dto.QuizCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = QuizController.class,
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
class QuizControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizService quizService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("QuizController test start");
    }

    private User createTeacher() {
        User teacher = new User("teacher", "teacher@test.com", "password");
        teacher.setUserId(1L);
        teacher.setRole(UserRole.TEACHER);
        return teacher;
    }

    private QuizResponse createTestQuizResponse() {
        return new QuizResponse(
            1L, "Quiz Title", "Quiz Desc", "en", "teacher", "TEACHER", "Math", 1,
            List.of(new FlashcardResponse(1L, "Q1?", "A1"))
        );
    }

    // --- GET /api/v1/quizzes/{id} ---

    @Test
    @DisplayName("getQuizById(): returns quiz response")
    void getQuizById_success() throws Exception {
        QuizResponse response = createTestQuizResponse();
        when(this.quizService.getQuizById(1L)).thenReturn(response);

        this.mockMvc.perform(get("/api/v1/quizzes/1")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.title").value("Quiz Title"))
            .andExpect(jsonPath("$.data.creatorUsername").value("teacher"))
            .andExpect(jsonPath("$.data.cardCount").value(1));

        verify(this.quizService, times(1)).getQuizById(1L);
    }

    @Test
    @DisplayName("getQuizById(): not found returns 404")
    void getQuizById_notFound_returns404() throws Exception {
        when(this.quizService.getQuizById(99L))
            .thenThrow(new ResourceNotFoundException("Quiz", "Quiz with ID 99 not found."));

        this.mockMvc.perform(get("/api/v1/quizzes/99")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isNotFound());
    }

    // --- GET /api/v1/quizzes/search ---

    @Test
    @DisplayName("searchQuizzes(): returns list of quizzes")
    void searchQuizzes_success() throws Exception {
        QuizSeachResponse r1 = new QuizSeachResponse(1L, "Quiz 1", "Desc 1", "en", "teacher", "TEACHER", "Math", 2);
        QuizSeachResponse r2 = new QuizSeachResponse(2L, "Quiz 2", "Desc 2", "en", "teacher", "TEACHER", "Physics", 3);

        when(this.quizService.searchQuizzes()).thenReturn(List.of(r1, r2));

        this.mockMvc.perform(get("/api/v1/quizzes/search")
                .with(user("user").roles("STUDENT")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));

        verify(this.quizService, times(1)).searchQuizzes();
    }

    // --- POST /api/v1/quizzes ---

    @Test
    @DisplayName("createQuiz(): authenticated user creates quiz successfully")
    void createQuiz_authenticated_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        List<FlashcardCreationRequest> flashcards = List.of(
            new FlashcardCreationRequest("Q1?", "A1")
        );
        QuizCreationRequest request = new QuizCreationRequest("New Quiz", "Desc", "en", flashcards, "Math");
        QuizResponse response = createTestQuizResponse();

        when(this.quizService.createQuiz(eq(1L), any(QuizCreationRequest.class))).thenReturn(response);

        this.mockMvc.perform(post("/api/v1/quizzes")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz created successfully."))
            .andExpect(jsonPath("$.data.title").value("Quiz Title"));

        verify(this.quizService, times(1)).createQuiz(eq(1L), any(QuizCreationRequest.class));
    }

    @Test
    @DisplayName("createQuiz(): unauthenticated returns forbidden")
    void createQuiz_unauthenticated_forbidden() throws Exception {
        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", "en", null, "Math");

        this.mockMvc.perform(post("/api/v1/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    // --- PUT /api/v1/quizzes/{id} ---

    @Test
    @DisplayName("updateQuiz(): authenticated user updates quiz successfully")
    void updateQuiz_authenticated_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        QuizCreationRequest request = new QuizCreationRequest("Updated Quiz", "New Desc", "en", null, "Math");
        QuizResponse response = new QuizResponse(
            1L, "Updated Quiz", "New Desc", "en", "teacher", "TEACHER", "Math", 0, List.of()
        );

        when(this.quizService.updateQuiz(eq(1L), eq(1L), any(QuizCreationRequest.class))).thenReturn(response);

        this.mockMvc.perform(put("/api/v1/quizzes/1")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz updated successfully."))
            .andExpect(jsonPath("$.data.title").value("Updated Quiz"));
    }



    @Test
    @DisplayName("updateQuiz(): not found returns 404")
    void updateQuiz_notFound_returns404() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", "en", null, "Math");

        when(this.quizService.updateQuiz(eq(99L), eq(1L), any(QuizCreationRequest.class)))
            .thenThrow(new ResourceNotFoundException("Quiz", "Quiz with ID 99 not found."));

        this.mockMvc.perform(put("/api/v1/quizzes/99")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateQuiz(): unauthenticated returns forbidden")
    void updateQuiz_unauthenticated_forbidden() throws Exception {
        QuizCreationRequest request = new QuizCreationRequest("Quiz", "Desc", "en", null, "Math");

        this.mockMvc.perform(put("/api/v1/quizzes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    // --- DELETE /api/v1/quizzes/{id} ---

    @Test
    @DisplayName("deleteQuiz(): successfully deletes quiz")
    void deleteQuiz_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        doNothing().when(this.quizService).deleteQuiz(1L, 1L);

        this.mockMvc.perform(delete("/api/v1/quizzes/1")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Quiz deleted successfully."));

        verify(this.quizService, times(1)).deleteQuiz(1L, 1L);
    }



    @Test
    @DisplayName("deleteQuiz(): not found returns 404")
    void deleteQuiz_notFound_returns404() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        doThrow(new ResourceNotFoundException("Quiz", "Quiz with ID 99 not found."))
            .when(this.quizService).deleteQuiz(eq(99L), anyLong());

        this.mockMvc.perform(delete("/api/v1/quizzes/99")
                .with(user(userDetails)))
            .andExpect(status().isNotFound());
    }

    // --- GET /api/v1/quizzes/me ---

    @Test
    @DisplayName("getQuizzesByUser(): returns user's quizzes")
    void getQuizzesByUser_success() throws Exception {
        User teacher = createTeacher();
        CustomUserDetails userDetails = new CustomUserDetails(teacher);

        QuizResponse r1 = createTestQuizResponse();
        QuizResponse r2 = new QuizResponse(
            2L, "Quiz 2", "Desc 2", "en", "teacher", "TEACHER", "Math", 0, List.of()
        );

        when(this.quizService.getQuizzesByUser(1L)).thenReturn(List.of(r1, r2));

        this.mockMvc.perform(get("/api/v1/quizzes/me")
                .with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));

        verify(this.quizService, times(1)).getQuizzesByUser(1L);
    }

    @Test
    @DisplayName("getQuizzesByUser(): unauthenticated returns forbidden")
    void getQuizzesByUser_unauthenticated_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/quizzes/me"))
            .andExpect(status().isForbidden());
    }
}


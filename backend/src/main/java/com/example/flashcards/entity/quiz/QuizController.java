package com.example.flashcards.entity.quiz;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.quiz.dto.QuizCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizResponse;
import com.example.flashcards.security.CustomUserDetails;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * get quiz by id.
     * @param id quiz id
     * @return quiz response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getQuizById(@PathVariable long id) {
        QuizResponse response = quizService.getQuizById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * search quizzes.
     * @param title optional title filter
     * @param username optional creator username filter
     * @return list of matching quizzes
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> searchQuizzes(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String username
    ) {
        List<QuizResponse> response = quizService.searchQuizzes(title, username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * create new quizzie.
     * @param userDetails the authenticated user
     * @param request the quiz creation request
     * @return the created quiz
     */
    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody QuizCreationRequest request
    ) {
        QuizResponse response = quizService.createQuiz(userDetails.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Quiz created successfully."));
    }

    /**
     * update adn existing quiz
     * @param id the id of the quiz
     * @param userDetails the authenticated user
     * @param request the quiz update request
     * @return the updated quiz
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> updateQuiz(
        @PathVariable long id,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody QuizCreationRequest request
    ) {
        QuizResponse response = quizService.updateQuiz(id, userDetails.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Quiz updated successfully."));
    }

    /**
     * delete quiz.
     * @param id quiz id to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Quiz deleted successfully."));
    }
}

package com.example.flashcards.entity.quizresult;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.quizresult.dto.QuizResultCreationRequest;
import com.example.flashcards.entity.quizresult.dto.QuizResultResponse;
import com.example.flashcards.security.CustomUserDetails;
import java.util.List;
import java.util.Locale;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/quiz-results")
public class QuizResultController {

    private final QuizResultService quizResultService;
    private final MessageSource messageSource;

    public QuizResultController(QuizResultService quizResultService, MessageSource messageSource) {
        this.quizResultService = quizResultService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResultResponse>> createQuizResult(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody QuizResultCreationRequest request,
        Locale locale
    ) {
        QuizResult result = quizResultService.createQuizResult(
            userDetails.getUserId(),
            request.quizId(),
            request.scorePercentage()
        );

        QuizResultResponse response = QuizResultResponse.from(result);

        return ResponseEntity.ok(ApiResponse.success(response, messageSource.getMessage("success.quizResult.saved", null, locale)));
    }

    @GetMapping("/me/quiz/{quizId}")
    public ResponseEntity<ApiResponse<List<QuizResultResponse>>> getQuizResultByQuizAndUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long quizId,
        Locale locale
    ) {
        List<QuizResult> results = quizResultService.getQuizResultByQuizAndUser(
            userDetails.getUserId(),
            quizId
        );

        List<QuizResultResponse> responses = results.stream()
            .map(QuizResultResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(responses, messageSource.getMessage("success.quizResult.fetched", null, locale)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<QuizResultResponse>>> getQuizResultByUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Locale locale
    ) {
        List<QuizResult> results = quizResultService.getQuizResultByUser(userDetails.getUserId());

        List<QuizResultResponse> response = results.stream()
            .map(QuizResultResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(response, messageSource.getMessage("success.quizResult.fetched", null, locale)));
    }
}

package com.example.flashcards.entity.quiz;

import java.util.List;
import com.example.flashcards.entity.quiz.dto.QuizCreationRequest;
import com.example.flashcards.entity.quiz.dto.QuizResponse;
import com.example.flashcards.entity.quiz.dto.QuizSeachResponse;

public interface IQuizService {
    QuizResponse getQuizById(long id);

    List<QuizSeachResponse> searchQuizzes();

    QuizResponse createQuiz(long userId, QuizCreationRequest request);

    QuizResponse updateQuiz(long id, long userId, QuizCreationRequest request);

    void deleteQuiz(long id);

    List<QuizSeachResponse> getQuizzesByUser(long userId);
}

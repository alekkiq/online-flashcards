package com.example.flashcards.entity.quizresult;

import java.util.List;

public interface IQuizResultService {
    QuizResult createQuizResult(long userId, long quizId, double scorePercentage);
    List<QuizResult> getQuizResultByQuizAndUser(Long userId, Long quizId);
    List<QuizResult> getQuizResultByUser(Long userId);
}

package com.example.flashcards.entity.quizresult;

import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUser(User user);
    List<QuizResult> findByQuizAndUser(Quiz quiz, User user);
}

package com.example.flashcards.entity.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q")
    List<Quiz> getAllQuizzes();

    List<Quiz> findByCreator_UserId(long userId);
}

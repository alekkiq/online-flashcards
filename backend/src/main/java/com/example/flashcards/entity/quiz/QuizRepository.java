package com.example.flashcards.entity.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q WHERE " +
           "(:title IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:username IS NULL OR LOWER(q.creator.username) LIKE LOWER(CONCAT('%', :username, '%')))")
    List<Quiz> searchQuizzes(@Param("title") String title, @Param("username") String username);
}

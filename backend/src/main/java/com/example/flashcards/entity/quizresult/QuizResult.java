package com.example.flashcards.entity.quizresult;

import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
@EntityListeners(AuditingEntityListener.class)
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizResultId;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(nullable = false)
    private double scorePercentage;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime takenAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    protected QuizResult() {}
    
    public QuizResult(double scorePercentage, LocalDateTime takenAt, User user, Quiz quiz) {
        this.scorePercentage = scorePercentage;
        this.takenAt = takenAt;
        this.user = user;
        this.quiz = quiz;
    }
    
    public long getQuizResultId() {
        return this.quizResultId;
    }
    
    public double getScorePercentage() {
        return this.scorePercentage;
    }
    
    public LocalDateTime getTakenAt() {
        return this.takenAt;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public Quiz getQuiz() {
        return this.quiz;
    }
    
    public void setScorePercentage(double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }
    
    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

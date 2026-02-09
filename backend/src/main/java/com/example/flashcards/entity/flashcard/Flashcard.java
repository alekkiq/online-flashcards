package com.example.flashcards.entity.flashcard;

import com.example.flashcards.entity.quiz.Quiz;
import jakarta.persistence.*;

@Entity
@Table(name = "flashcards")
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long flashcardId;

    @Column(nullable = false, length = 255)
    private String question;

    @Column(nullable = false, length = 255)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    protected Flashcard() {
    }

    public Flashcard(String question, String answer, Quiz quiz) {
        this.question = question;
        this.answer = answer;
        this.quiz = quiz;
    }

    public long getFlashcardId() {
        return this.flashcardId;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setFlashcardId(long flashcardId) {
        this.flashcardId = flashcardId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

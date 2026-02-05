package com.example.flashcards.entity.quiz;

import com.example.flashcards.entity.flashcard.Flashcard;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = true, length = 255)
    private String description;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Flashcard> flashcards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    protected Quiz() {
    }

    public Quiz(String title, String description, User creator, Subject subject) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.subject = subject;
    }

    public long getQuizId() {
        return this.quizId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Flashcard> getFlashcards() {
        return this.flashcards;
    }

    public User getCreator() {
        return this.creator;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
        flashcard.setQuiz(this);
    }

    public void removeFlashcard(Flashcard flashcard) {
        flashcards.remove(flashcard);
        flashcard.setQuiz(null);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId=" + quizId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator.getUsername() +
                ", subject=" + subject.getName() +
                '}';
    }
}

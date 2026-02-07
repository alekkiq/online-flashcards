package com.example.flashcards.entity.classroom;

import com.example.flashcards.entity.learningmaterial.LearningMaterial;
import com.example.flashcards.entity.quiz.Quiz;
import com.example.flashcards.entity.subject.Subject;
import com.example.flashcards.entity.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = true, length = 255)
    private String note;

    @Column(nullable = true, length = 255)
    private String joinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
        name = "classroom_users",
        joinColumns = @JoinColumn(name = "classroom_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "classroom_quizzes",
        joinColumns = @JoinColumn(name = "classroom_id"),
        inverseJoinColumns = @JoinColumn(name = "quiz_id")
    )
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningMaterial> learningMaterials = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    protected Classroom() {}

    public Classroom(String title, String description, String note, String joinCode, User owner, Subject subject) {
        this.title = title;
        this.description = description;
        this.note = note;
        this.joinCode = joinCode;
        this.owner = owner;
        this.subject = subject;
    }

    public long getClassroomId() {
        return this.classroomId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getNote() {
        return this.note;
    }

    public String getJoinCode() {
        return this.joinCode;
    }

    public User getOwner() {
        return this.owner;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public List<LearningMaterial> getLearningMaterials() {
        return this.learningMaterials;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setClassroomId(long classroomId) {
        this.classroomId = classroomId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
    }

    public void removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
    }

    public void addLearningMaterial(LearningMaterial learningMaterial) {
        learningMaterials.add(learningMaterial);
        learningMaterial.setClassroom(this);
    }

    public void removeLearningMaterial(LearningMaterial learningMaterial) {
        learningMaterials.remove(learningMaterial);
        learningMaterial.setClassroom(null);
    }
}

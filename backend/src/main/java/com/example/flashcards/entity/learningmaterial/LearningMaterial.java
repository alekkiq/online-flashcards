package com.example.flashcards.entity.learningmaterial;

import com.example.flashcards.entity.classroom.Classroom;
import com.example.flashcards.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "learning_materials")
public class LearningMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long learningMaterialId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = true)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    protected LearningMaterial() {}

    public LearningMaterial(String title, String content, Classroom classroom, User creator) {
        this.title = title;
        this.content = content;
        this.classroom = classroom;
        this.creator = creator;
    }

    public long getLearningMaterialId() {
        return this.learningMaterialId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}

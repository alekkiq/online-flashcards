package com.example.flashcards.entity.subject;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectId;

    @Column(nullable = false, length = 255)
    private String name;

    protected Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public long getSubjectId() {
        return this.subjectId;
    }

    public String getName() {
        return this.name;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public void setName(String name) {
        this.name = name;
    }
}

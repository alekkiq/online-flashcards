package com.example.flashcards.entity.subject;

import jakarta.persistence.*;

@Entity
@Table(
    name = "subjects",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "language"})
    }
)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subjectId;

    @Column(nullable = false, length = 255)
    private String code; // common code between all languages

    @Column(nullable = false, length = 255)
    private String name; // localized name/label of the subject

    @Column(nullable = false, length = 10)
    private String language;

    protected Subject() {
    }

    public Subject(String code, String name, String language) {
        this.code = code;
        this.name = name;
        this.language = language;
    }

    public long getSubjectId() {
        return this.subjectId;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

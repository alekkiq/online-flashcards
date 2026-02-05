package com.example.flashcards.entity.flashcard;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
}

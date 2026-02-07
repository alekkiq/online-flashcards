package com.example.flashcards;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class OnlineFlashcardsApi {
    public static void main(String[] args) {
        SpringApplication.run(OnlineFlashcardsApi.class, args);
    }
}
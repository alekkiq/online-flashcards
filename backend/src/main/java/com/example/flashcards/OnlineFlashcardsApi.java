package com.example.flashcards;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.flashcards.config")
public class OnlineFlashcardsApi {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        Dotenv parentDotenv = Dotenv.configure()
            .directory("../")
            .ignoreIfMissing()
            .load();
        parentDotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(OnlineFlashcardsApi.class, args);
    }
}
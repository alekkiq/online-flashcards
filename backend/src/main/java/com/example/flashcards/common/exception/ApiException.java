package com.example.flashcards.common.exception;

public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}

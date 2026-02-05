package com.example.flashcards.common.exception;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}

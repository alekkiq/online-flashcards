package com.example.flashcards.common.exceptions;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}

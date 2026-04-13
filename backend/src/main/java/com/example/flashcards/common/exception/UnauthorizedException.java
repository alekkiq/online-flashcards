package com.example.flashcards.common.exception;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String messageKey, Object[] args) {
        super(messageKey, args);
    }
}

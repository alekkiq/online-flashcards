package com.example.flashcards.common.exception;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String messageKey, Object[] args) {
        super(messageKey, args);
    }
}

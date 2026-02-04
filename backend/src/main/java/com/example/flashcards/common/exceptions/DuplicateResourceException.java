package com.example.flashcards.common.exceptions;

public class DuplicateResourceException extends ApiException {
    private final String resourceName;

    public DuplicateResourceException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return this.resourceName;
    }
}

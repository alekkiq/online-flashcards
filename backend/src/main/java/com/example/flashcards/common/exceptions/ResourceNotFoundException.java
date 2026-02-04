package com.example.flashcards.common.exceptions;

public class ResourceNotFoundException extends ApiException {
    private final String resourceName;
    private final Long resourceId;

    public ResourceNotFoundException(String resourceName, Long id, String message) {
        super(message);
        this.resourceName = resourceName;
        this.resourceId = id;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public Long getResourceId() {
        return this.resourceId;
    }
}

package com.example.flashcards.common.exception;

public abstract class ApiException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public ApiException(String message) {
        super(message);
        this.messageKey = null;
        this.args = null;
    }

    public ApiException(String messageKey, Object[] args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public Object[] getArgs() {
        return this.args;
    }
}

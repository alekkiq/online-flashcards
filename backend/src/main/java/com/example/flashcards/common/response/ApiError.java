package com.example.flashcards.common.response;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ApiError(HttpStatus code, String message, Map<String, Object> details) {
    public ApiError(HttpStatus code, String message) {
        this(code, message, null);
    }
}

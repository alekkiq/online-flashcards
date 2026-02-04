package com.example.flashcards.common;

import java.util.Map;

public record ApiError(String code, String message, Map<String, Object> details) {
    public ApiError(String code, String message) {
        this(code, message, null);
    }
}

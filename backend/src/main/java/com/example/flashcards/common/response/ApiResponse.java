package com.example.flashcards.common.response;

public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final ApiError error;

    private ApiResponse(boolean success, T data, String message, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> failure(ApiError error) {
        return new ApiResponse<>(false, null, null, error);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public ApiError getError() {
        return this.error;
    }
}

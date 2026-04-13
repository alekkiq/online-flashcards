package com.example.flashcards.common.handler;

import com.example.flashcards.common.exception.*;
import com.example.flashcards.common.response.ApiError;
import com.example.flashcards.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String resolve(ApiException ex, Locale locale) {
        if (ex.getMessageKey() != null) {
            return messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);
        }
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex, Locale locale) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, resolve(ex, locale));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex, Locale locale) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, resolve(ex, locale));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex, Locale locale) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, resolve(ex, locale));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex, Locale locale) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, resolve(ex, locale));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException ex, Locale locale) {
        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED,
            messageSource.getMessage("error.auth.required", null, locale)
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(error));
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(Exception ex, Locale locale) {
        ApiError error = new ApiError(
            HttpStatus.FORBIDDEN,
            messageSource.getMessage("error.access.denied", null, locale)
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST,
            messageSource.getMessage("validation.request.invalid", null, locale),
            details
        );

        return ResponseEntity.badRequest().body(ApiResponse.failure(error));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex, Locale locale) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.failure(error));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointer(NullPointerException ex, Locale locale) {
        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED,
            messageSource.getMessage("error.auth.required", null, locale)
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex, Locale locale) {
        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            messageSource.getMessage("error.unexpected", null, locale)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(error));
    }
}


package com.example.flashcards.entity.promotionrequest.dto;

import com.example.flashcards.entity.promotionrequest.PromotionRequest;
import com.example.flashcards.entity.promotionrequest.PromotionRequestStatus;

import java.time.LocalDateTime;

public record PromotionRequestResponse(
    long promotionRequestId,
    String message,
    PromotionRequestStatus status,
    LocalDateTime requestedAt,
    LocalDateTime reviewedAt,
    long userId,
    String username
) {
    public static PromotionRequestResponse from(PromotionRequest request) {
        return new PromotionRequestResponse(
            request.getPromotionRequestId(),
            request.getMessage(),
            request.getStatus(),
            request.getRequestedAt(),
            request.getReviewedAt(),
            request.getUser().getUserId(),
            request.getUser().getUsername()
        );
    }
}

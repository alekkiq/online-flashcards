package com.example.flashcards.entity.promotionrequest.dto;

import com.example.flashcards.entity.promotionrequest.PromotionRequestStatus;
import jakarta.validation.constraints.NotNull;

public record PromotionRequestUpdateRequest(
    @NotNull(message = "Status is required")
    PromotionRequestStatus status
) {}

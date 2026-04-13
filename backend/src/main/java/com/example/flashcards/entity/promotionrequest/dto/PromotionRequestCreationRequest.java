package com.example.flashcards.entity.promotionrequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PromotionRequestCreationRequest(
    @NotBlank(message = "validation.promotionRequest.message.required")
    @Size(max = 500, message = "validation.promotionRequest.message.maxLength")
    String message
) {}

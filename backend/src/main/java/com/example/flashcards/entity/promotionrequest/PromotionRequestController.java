package com.example.flashcards.entity.promotionrequest;

import com.example.flashcards.common.response.ApiResponse;
import com.example.flashcards.entity.promotionrequest.dto.PromotionRequestCreationRequest;
import com.example.flashcards.entity.promotionrequest.dto.PromotionRequestResponse;
import com.example.flashcards.entity.promotionrequest.dto.PromotionRequestUpdateRequest;
import com.example.flashcards.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotion-requests")
public class PromotionRequestController {
    private final PromotionRequestService promotionRequestService;

    public PromotionRequestController(PromotionRequestService promotionRequestService) {
        this.promotionRequestService = promotionRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromotionRequestResponse>> createRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PromotionRequestCreationRequest request) {
        Long userId = userDetails.getUserId();
        PromotionRequest createdRequest = this.promotionRequestService.createRequest(userId, request.message());
        PromotionRequestResponse response = PromotionRequestResponse.from(createdRequest);

        return ResponseEntity.ok(ApiResponse.success(response, "Promotion request created successfully"));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PromotionRequestResponse>>> getPendingRequests() {
        List<PromotionRequest> pendingRequests = this.promotionRequestService.getPendingRequests();
        List<PromotionRequestResponse> response = pendingRequests.stream()
            .map(PromotionRequestResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse<List<PromotionRequestResponse>>> getMyRequests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<PromotionRequest> userRequests = this.promotionRequestService.getRequestsByUser(userId);
        List<PromotionRequestResponse> response = userRequests.stream()
            .map(PromotionRequestResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PromotionRequestResponse>> updateRequest(
        @PathVariable Long id,
        @Valid @RequestBody PromotionRequestUpdateRequest request
    ) {
        PromotionRequest promotionRequest = switch (request.status()) {
            case APPROVED -> this.promotionRequestService.approveRequest(id);
            case REJECTED -> this.promotionRequestService.rejectRequest(id);
            default -> throw new IllegalArgumentException("Invalid status: " + request.status());
        };
        return ResponseEntity.ok(ApiResponse.success(PromotionRequestResponse.from(promotionRequest), "Promotion request updated successfully"));
    }
}

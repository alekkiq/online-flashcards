package com.example.flashcards.entity.promotionrequest;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionRequestService {
    private final PromotionRequestRepository promotionRequestRepository;
    private final UserRepository userRepository;

    public PromotionRequestService(PromotionRequestRepository promotionRequestRepository, UserRepository userRepository) {
        this.promotionRequestRepository = promotionRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PromotionRequest createRequest(Long userId, String message) {
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId, "User with ID " + userId + "not found"));

        PromotionRequest request = new PromotionRequest();
        request.setUser(user);
        request.setStatus(PromotionRequestStatus.PENDING);
        request.setMessage(message);

        return this.promotionRequestRepository.save(request);
    }

    @Transactional
    public PromotionRequest approveRequest(Long promotionRequestId) {
        PromotionRequest request = this.promotionRequestRepository.findById(promotionRequestId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRequest", promotionRequestId, "Promotion request with ID " + promotionRequestId + " not found"));

        request.setStatus(PromotionRequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());

        return this.promotionRequestRepository.save(request);
    }

    @Transactional
    public PromotionRequest rejectRequest(Long promotionRequestId) {
        PromotionRequest request = this.promotionRequestRepository.findById(promotionRequestId)
            .orElseThrow(() -> new ResourceNotFoundException("PromotionRequest", promotionRequestId, "Promotion request with ID " + promotionRequestId + " not found"));

        request.setStatus(PromotionRequestStatus.REJECTED);
        request.setReviewedAt(LocalDateTime.now());

        return this.promotionRequestRepository.save(request);
    }

    public List<PromotionRequest> getPendingRequests() {
        return this.promotionRequestRepository.findByStatus(PromotionRequestStatus.PENDING);
    }

    public List<PromotionRequest> getRequestsByUser(Long userId) {
        return this.promotionRequestRepository.findByUser_UserId(userId);
    }
}

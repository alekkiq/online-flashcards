package com.example.flashcards.entity.promotionrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, Long> {
    List<PromotionRequest> findByUser_UserId(Long userId);
    List<PromotionRequest> findByStatus(PromotionRequestStatus status);
}

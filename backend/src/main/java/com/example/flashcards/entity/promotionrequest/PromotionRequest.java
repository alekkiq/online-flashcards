package com.example.flashcards.entity.promotionrequest;

import com.example.flashcards.entity.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_requests")
public class PromotionRequest {
    private static final PromotionRequestStatus DEFAULT_STATUS = PromotionRequestStatus.PENDING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long promotionRequestId;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionRequestStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected PromotionRequest() {}

    public PromotionRequest(String message, User user) {
        this.message = message;
        this.user = user;
        this.status = DEFAULT_STATUS;
    }

    public long getPromotionRequestId() {
        return this.promotionRequestId;
    }

    public String getMessage() {
        return this.message;
    }

    public PromotionRequestStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getRequestedAt() {
        return this.requestedAt;
    }

    public LocalDateTime getReviewedAt() {
        return this.reviewedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(PromotionRequestStatus status) {
        this.status = status;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPromotionRequestId(long promotionRequestId) {
        this.promotionRequestId = promotionRequestId;
    }
}

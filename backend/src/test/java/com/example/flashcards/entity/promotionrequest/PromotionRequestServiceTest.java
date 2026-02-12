package com.example.flashcards.entity.promotionrequest;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.entity.user.UserRepository;
import com.example.flashcards.entity.user.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PromotionRequestServiceTest {
    private PromotionRequestRepository promotionRequestRepository;
    private UserRepository userRepository;
    private PromotionRequestService promotionRequestService;

    @BeforeAll
    static void initAll() {
        System.out.println("PromotionRequestService test start");
    }

    @BeforeEach
    void setup() {
        this.promotionRequestRepository = Mockito.mock(PromotionRequestRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.promotionRequestService = new PromotionRequestService(this.promotionRequestRepository, this.userRepository);
    }

    @Test
    @DisplayName("createRequest(): successful request creation")
    void createRequestSuccess() {
        User user = new User("student1", "student1@test.com", "password");
        user.setUserId(1L);
        user.setRole(UserRole.STUDENT);

        String message = "I would like to become a teacher";

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        PromotionRequest result = this.promotionRequestService.createRequest(1L, message);

        assertEquals(user, result.getUser());
        assertEquals(PromotionRequestStatus.PENDING, result.getStatus());
        assertEquals(message, result.getMessage());
        verify(this.promotionRequestRepository, times(1)).save(any(PromotionRequest.class));
    }

    @Test
    @DisplayName("createRequest(): user not found throws exception")
    void createRequest_userNotFound_throwsException() {
        when(this.userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            this.promotionRequestService.createRequest(99L, "Message"));
        verify(this.promotionRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("createRequest(): creates request with null message")
    void createRequest_nullMessage() {
        User user = new User("student2", "student2@test.com", "password");
        user.setUserId(2L);

        when(this.userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        PromotionRequest result = this.promotionRequestService.createRequest(2L, null);

        assertNull(result.getMessage());
        assertEquals(PromotionRequestStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("approveRequest(): successful approval")
    void approveRequestSuccess() {
        User user = new User("student3", "student3@test.com", "password");
        PromotionRequest request = new PromotionRequest("Please approve", user);
        request.setPromotionRequestId(5L);

        when(this.promotionRequestRepository.findById(5L)).thenReturn(Optional.of(request));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        PromotionRequest result = this.promotionRequestService.approveRequest(5L);

        assertEquals(PromotionRequestStatus.APPROVED, result.getStatus());
        verify(this.promotionRequestRepository, times(1)).save(request);
    }

    @Test
    @DisplayName("approveRequest(): request not found throws exception")
    void approveRequest_requestNotFound_throwsException() {
        when(this.promotionRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            this.promotionRequestService.approveRequest(99L));
        verify(this.promotionRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("approveRequest(): changes status from PENDING to APPROVED")
    void approveRequest_statusChange() {
        User user = new User("student4", "student4@test.com", "password");
        PromotionRequest request = new PromotionRequest("Request", user);
        request.setPromotionRequestId(10L);

        when(this.promotionRequestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        this.promotionRequestService.approveRequest(10L);

        ArgumentCaptor<PromotionRequest> captor = ArgumentCaptor.forClass(PromotionRequest.class);
        verify(this.promotionRequestRepository, times(1)).save(captor.capture());
        assertEquals(PromotionRequestStatus.APPROVED, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("rejectRequest(): successful rejection")
    void rejectRequestSuccess() {
        User user = new User("student5", "student5@test.com", "password");
        PromotionRequest request = new PromotionRequest("Please reject", user);
        request.setPromotionRequestId(7L);

        when(this.promotionRequestRepository.findById(7L)).thenReturn(Optional.of(request));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        PromotionRequest result = this.promotionRequestService.rejectRequest(7L);

        assertEquals(PromotionRequestStatus.REJECTED, result.getStatus());
        verify(this.promotionRequestRepository, times(1)).save(request);
    }

    @Test
    @DisplayName("rejectRequest(): request not found throws exception")
    void rejectRequest_requestNotFound_throwsException() {
        when(this.promotionRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            this.promotionRequestService.rejectRequest(99L));
        verify(this.promotionRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("rejectRequest(): changes status from PENDING to REJECTED")
    void rejectRequest_statusChange() {
        User user = new User("student6", "student6@test.com", "password");
        PromotionRequest request = new PromotionRequest("Request", user);
        request.setPromotionRequestId(11L);

        when(this.promotionRequestRepository.findById(11L)).thenReturn(Optional.of(request));
        when(this.promotionRequestRepository.save(any(PromotionRequest.class))).thenAnswer(i -> i.getArgument(0));

        this.promotionRequestService.rejectRequest(11L);

        ArgumentCaptor<PromotionRequest> captor = ArgumentCaptor.forClass(PromotionRequest.class);
        verify(this.promotionRequestRepository, times(1)).save(captor.capture());
        assertEquals(PromotionRequestStatus.REJECTED, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("getPendingRequests(): returns list of pending requests")
    void getPendingRequestsReturnsList() {
        User user1 = new User("student7", "student7@test.com", "password");
        User user2 = new User("student8", "student8@test.com", "password");

        PromotionRequest request1 = new PromotionRequest("Message 1", user1);
        PromotionRequest request2 = new PromotionRequest("Message 2", user2);

        when(this.promotionRequestRepository.findByStatus(PromotionRequestStatus.PENDING))
            .thenReturn(List.of(request1, request2));

        List<PromotionRequest> result = this.promotionRequestService.getPendingRequests();

        assertEquals(2, result.size());
        assertEquals(PromotionRequestStatus.PENDING, result.get(0).getStatus());
        assertEquals(PromotionRequestStatus.PENDING, result.get(1).getStatus());
        verify(this.promotionRequestRepository, times(1)).findByStatus(PromotionRequestStatus.PENDING);
    }

    @Test
    @DisplayName("getPendingRequests(): returns empty list when no pending requests")
    void getPendingRequests_emptyList() {
        when(this.promotionRequestRepository.findByStatus(PromotionRequestStatus.PENDING))
            .thenReturn(List.of());

        List<PromotionRequest> result = this.promotionRequestService.getPendingRequests();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getRequestsByUser(): returns list of user's requests")
    void getRequestsByUserReturnsList() {
        User user = new User("student9", "student9@test.com", "password");
        user.setUserId(15L);

        PromotionRequest request1 = new PromotionRequest("Message 1", user);
        PromotionRequest request2 = new PromotionRequest("Message 2", user);
        PromotionRequest request3 = new PromotionRequest("Message 3", user);

        when(this.promotionRequestRepository.findByUser_UserId(15L))
            .thenReturn(List.of(request1, request2, request3));

        List<PromotionRequest> result = this.promotionRequestService.getRequestsByUser(15L);

        assertEquals(3, result.size());
        assertEquals(user, result.get(0).getUser());
        assertEquals(user, result.get(1).getUser());
        assertEquals(user, result.get(2).getUser());
        verify(this.promotionRequestRepository, times(1)).findByUser_UserId(15L);
    }

    @Test
    @DisplayName("getRequestsByUser(): returns empty list when user has no requests")
    void getRequestsByUser_emptyList() {
        when(this.promotionRequestRepository.findByUser_UserId(20L))
            .thenReturn(List.of());

        List<PromotionRequest> result = this.promotionRequestService.getRequestsByUser(20L);

        assertTrue(result.isEmpty());
    }
}
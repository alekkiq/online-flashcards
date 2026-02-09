package com.example.flashcards.entity.promotionrequest;

import com.example.flashcards.common.exception.ResourceNotFoundException;
import com.example.flashcards.config.SecurityConfig;
import com.example.flashcards.config.TestSecurityConfig;
import com.example.flashcards.entity.promotionrequest.dto.PromotionRequestCreationRequest;
import com.example.flashcards.entity.promotionrequest.dto.PromotionRequestUpdateRequest;
import com.example.flashcards.entity.subject.SubjectController;
import com.example.flashcards.entity.user.User;
import com.example.flashcards.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SubjectController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            SecurityConfig.class,
            JwtAuthFilter.class,
            JwtService.class,
            CustomUserDetailsService.class,
            CustomAuthenticationEntryPoint.class,
            CustomAccessDeniedHandler.class
        }
    )
)
@Import(TestSecurityConfig.class)
class PromotionRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromotionRequestService promotionRequestService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("PromotionRequestController test start");
    }

    @Test
    @DisplayName("createRequest(): successfully creates promotion request")
    void createRequest() throws Exception {
        User user = new User("tester", "student@test.com", "password");
        user.setUserId(1L);

        PromotionRequest request = new PromotionRequest("I want to teach", user);
        request.setPromotionRequestId(1L);
        request.setStatus(PromotionRequestStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        PromotionRequestCreationRequest creationRequest = new PromotionRequestCreationRequest("I want to teach");

        when(this.promotionRequestService.createRequest(eq(1L), anyString())).thenReturn(request);

        this.mockMvc.perform(post("/api/v1/promotion-requests")
                .with(user("1").roles("STUDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(creationRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Promotion request created successfully"))
            .andExpect(jsonPath("$.data.status").value("PENDING"))
            .andExpect(jsonPath("$.data.message").value("I want to teach"));

        verify(this.promotionRequestService, times(1)).createRequest(eq(1L), anyString());
    }

    @Test
    @DisplayName("createRequest(): fails with blank message")
    void createRequest_blankMessage_badRequest() throws Exception {
        PromotionRequestCreationRequest creationRequest = new PromotionRequestCreationRequest("");

        this.mockMvc.perform(post("/api/v1/promotion-requests")
                .with(user("1").roles("STUDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(creationRequest)))
            .andExpect(status().isBadRequest());

        verify(this.promotionRequestService, never()).createRequest(anyLong(), anyString());
    }

    @Test
    @DisplayName("getPendingRequests(): returns list of pending requests")
    void getPendingRequests() throws Exception {
        User user1 = new User("tester1", "student@test.com", "password");;
        user1.setUserId(1L);

        User user2 = new User("tester2", "student2@test.com", "passwordz");;
        user2.setUserId(2L);

        PromotionRequest request1 = new PromotionRequest("Request 1", user1);
        request1.setPromotionRequestId(1L);
        request1.setStatus(PromotionRequestStatus.PENDING);

        PromotionRequest request2 = new PromotionRequest("Request 2", user2);
        request2.setPromotionRequestId(2L);
        request2.setStatus(PromotionRequestStatus.PENDING);

        when(this.promotionRequestService.getPendingRequests()).thenReturn(List.of(request1, request2));

        this.mockMvc.perform(get("/api/v1/promotion-requests/pending")
                .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].status").value("PENDING"))
            .andExpect(jsonPath("$.data[1].status").value("PENDING"));

        verify(this.promotionRequestService, times(1)).getPendingRequests();
    }

    @Test
    @DisplayName("getPendingRequests(): forbidden without ADMIN role")
    void getPendingRequests_withoutAdminRole_forbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/promotion-requests/pending")
                .with(user("1").roles("STUDENT")))
            .andExpect(status().isForbidden());

        verify(this.promotionRequestService, never()).getPendingRequests();
    }

    @Test
    @DisplayName("getMyRequests(): returns user's promotion requests")
    void getMyRequests() throws Exception {
        User user = new User("tester", "student@test.com", "password");
        user.setUserId(1L);

        PromotionRequest request1 = new PromotionRequest("Request 1", user);
        request1.setPromotionRequestId(1L);
        request1.setStatus(PromotionRequestStatus.PENDING);

        PromotionRequest request2 = new PromotionRequest("Request 2", user);
        request2.setPromotionRequestId(2L);
        request2.setStatus(PromotionRequestStatus.APPROVED);

        when(this.promotionRequestService.getRequestsByUser(1L)).thenReturn(List.of(request1, request2));

        this.mockMvc.perform(get("/api/v1/promotion-requests/my-requests")
                        .with(user("1").roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(this.promotionRequestService, times(1)).getRequestsByUser(1L);
    }

    @Test
    @DisplayName("updateRequest(): successfully approves request")
    void updateRequest_approve() throws Exception {
        User user = new User("tester", "student@test.com", "password");;
        user.setUserId(1L);

        PromotionRequest request = new PromotionRequest("Request", user);
        request.setPromotionRequestId(1L);
        request.setStatus(PromotionRequestStatus.APPROVED);

        PromotionRequestUpdateRequest updateRequest = new PromotionRequestUpdateRequest(PromotionRequestStatus.APPROVED);

        when(this.promotionRequestService.approveRequest(1L)).thenReturn(request);

        this.mockMvc.perform(patch("/api/v1/promotion-requests/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Promotion request updated successfully"))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        verify(this.promotionRequestService, times(1)).approveRequest(1L);
        verify(this.promotionRequestService, never()).rejectRequest(anyLong());
    }

    @Test
    @DisplayName("updateRequest(): successfully rejects request")
    void updateRequest_reject() throws Exception {
        User user = new User("tester", "student@test.com", "password");;
        user.setUserId(1L);

        PromotionRequest request = new PromotionRequest("Request", user);
        request.setPromotionRequestId(1L);
        request.setStatus(PromotionRequestStatus.REJECTED);

        PromotionRequestUpdateRequest updateRequest = new PromotionRequestUpdateRequest(PromotionRequestStatus.REJECTED);

        when(this.promotionRequestService.rejectRequest(1L)).thenReturn(request);

        this.mockMvc.perform(patch("/api/v1/promotion-requests/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Promotion request updated successfully"))
            .andExpect(jsonPath("$.data.status").value("REJECTED"));

        verify(this.promotionRequestService, times(1)).rejectRequest(1L);
        verify(this.promotionRequestService, never()).approveRequest(anyLong());
    }

    @Test
    @DisplayName("updateRequest(): forbidden without ADMIN role")
    void updateRequest_withoutAdminRole_forbidden() throws Exception {
        PromotionRequestUpdateRequest updateRequest = new PromotionRequestUpdateRequest(PromotionRequestStatus.APPROVED);

        this.mockMvc.perform(patch("/api/v1/promotion-requests/1")
                .with(user("1").roles("STUDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isForbidden());

        verify(this.promotionRequestService, never()).approveRequest(anyLong());
        verify(this.promotionRequestService, never()).rejectRequest(anyLong());
    }

    @Test
    @DisplayName("updateRequest(): request not found throws exception")
    void updateRequest_requestNotFound_throwsException() throws Exception {
        PromotionRequestUpdateRequest updateRequest = new PromotionRequestUpdateRequest(PromotionRequestStatus.APPROVED);

        when(this.promotionRequestService.approveRequest(99L))
            .thenThrow(new ResourceNotFoundException("PromotionRequest", "Request not found"));

        this.mockMvc.perform(patch("/api/v1/promotion-requests/99")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound());

        verify(this.promotionRequestService, times(1)).approveRequest(99L);
    }
}

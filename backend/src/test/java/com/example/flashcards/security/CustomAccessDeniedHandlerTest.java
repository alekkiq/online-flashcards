package com.example.flashcards.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAccessDeniedHandlerTest {
    private CustomAccessDeniedHandler handler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("CustomAccessDeniedHandler test start");
    }

    @BeforeEach
    void setup() throws Exception {
        this.handler = new CustomAccessDeniedHandler();
        this.request = Mockito.mock(HttpServletRequest.class);
        this.response = Mockito.mock(HttpServletResponse.class);
        this.responseWriter = new StringWriter();
        when(this.response.getWriter()).thenReturn(new PrintWriter(this.responseWriter));
    }

    @Test
    @DisplayName("handle(): sets 403 status code")
    void handle_sets403Status() throws Exception {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        this.handler.handle(this.request, this.response, exception);

        verify(this.response).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("handle(): sets JSON content type")
    void handle_setsJsonContentType() throws Exception {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        this.handler.handle(this.request, this.response, exception);

        verify(this.response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("handle(): writes correct JSON response body")
    @SuppressWarnings("unchecked")
    void handle_writesCorrectJsonBody() throws Exception {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        this.handler.handle(this.request, this.response, exception);

        String json = this.responseWriter.toString();
        Map<String, Object> body = this.objectMapper.readValue(json, Map.class);

        assertEquals(false, body.get("success"));
        assertNotNull(body.get("error"));

        Map<String, Object> error = (Map<String, Object>) body.get("error");
        assertEquals("FORBIDDEN", error.get("code"));
        assertEquals("Access denied. You do not have permission to access this resource.", error.get("message"));
    }
}


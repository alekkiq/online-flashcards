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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {
    private CustomAuthenticationEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void initAll() {
        System.out.println("CustomAuthenticationEntryPoint test start");
    }

    @BeforeEach
    void setup() throws Exception {
        this.entryPoint = new CustomAuthenticationEntryPoint();
        this.request = Mockito.mock(HttpServletRequest.class);
        this.response = Mockito.mock(HttpServletResponse.class);
        this.responseWriter = new StringWriter();
        when(this.response.getWriter()).thenReturn(new PrintWriter(this.responseWriter));
    }

    @Test
    @DisplayName("commence(): sets 401 status code")
    void commence_sets401Status() throws Exception {
        AuthenticationException exception = new BadCredentialsException("Bad credentials");

        this.entryPoint.commence(this.request, this.response, exception);

        verify(this.response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("commence(): sets JSON content type")
    void commence_setsJsonContentType() throws Exception {
        AuthenticationException exception = new BadCredentialsException("Bad credentials");

        this.entryPoint.commence(this.request, this.response, exception);

        verify(this.response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("commence(): writes correct JSON response body")
    @SuppressWarnings("unchecked")
    void commence_writesCorrectJsonBody() throws Exception {
        AuthenticationException exception = new BadCredentialsException("Bad credentials");

        this.entryPoint.commence(this.request, this.response, exception);

        String json = this.responseWriter.toString();
        Map<String, Object> body = this.objectMapper.readValue(json, Map.class);

        assertEquals(false, body.get("success"));
        assertNotNull(body.get("error"));

        Map<String, Object> error = (Map<String, Object>) body.get("error");
        assertEquals("UNAUTHORIZED", error.get("code"));
        assertEquals("Authentication required. Please provide a valid token.", error.get("message"));
    }
}


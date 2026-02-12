package com.example.flashcards.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private JwtAuthFilter jwtAuthFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeAll
    static void initAll() {
        System.out.println("JwtAuthFilter test start");
    }

    @BeforeEach
    void setup() {
        this.jwtService = Mockito.mock(JwtService.class);
        this.userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        this.jwtAuthFilter = new JwtAuthFilter(this.jwtService, (CustomUserDetailsService) this.userDetailsService);
        this.request = Mockito.mock(HttpServletRequest.class);
        this.response = Mockito.mock(HttpServletResponse.class);
        this.filterChain = Mockito.mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("doFilterInternal(): valid JWT token successfully authenticates user")
    void doFilterInternal_validToken_authenticatesUser() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String username = "testuser";
        UserDetails userDetails = User
                .withUsername(username)
                .password("password")
                .roles("USER")
                .build();

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.extractUsername(token)).thenReturn(username);
        when(this.userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(this.jwtService.isValidToken(token, userDetails)).thenReturn(true);

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): missing Authorization header passes through")
    void doFilterInternal_missingHeader_passesThrough() throws ServletException, IOException {
        when(this.request.getHeader("Authorization")).thenReturn(null);

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
        verify(this.jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("doFilterInternal(): invalid header format without 'Bearer ' prefix")
    void doFilterInternal_invalidHeaderFormat_passesThrough() throws ServletException, IOException {
        when(this.request.getHeader("Authorization")).thenReturn("InvalidToken");

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
        verify(this.jwtService, never()).extractUsername(anyString());
    }

    @Test
    @DisplayName("doFilterInternal(): expired JWT token does not authenticate")
    void doFilterInternal_expiredToken_doesNotAuthenticate() throws ServletException, IOException {
        String token = "expired.jwt.token";
        String username = "testuser";
        UserDetails userDetails = User
                .withUsername(username)
                .password("password")
                .roles("USER")
                .build();

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.extractUsername(token)).thenReturn(username);
        when(this.userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(this.jwtService.isValidToken(token, userDetails)).thenReturn(false);

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): invalid JWT token does not authenticate")
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws ServletException, IOException {
        String token = "invalid.jwt.token";

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): token with non-existent user does not authenticate")
    void doFilterInternal_nonExistentUser_doesNotAuthenticate() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String username = "nonexistent";

        when(this.request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(this.jwtService.extractUsername(token)).thenReturn(username);
        when(this.userDetailsService.loadUserByUsername(username))
                .thenThrow(new RuntimeException("User not found"));

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): already authenticated request skips re-authentication")
    void doFilterInternal_alreadyAuthenticated_skipsReAuthentication() throws ServletException, IOException {
        String username = "testuser";
        UserDetails userDetails = User
                .withUsername(username)
                .password("password")
                .roles("USER")
                .build();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(this.request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        verify(this.jwtService, never()).extractUsername(anyString());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): filter chain always continues")
    void doFilterInternal_filterChainAlwaysContinues() throws ServletException, IOException {
        when(this.request.getHeader("Authorization")).thenReturn(null);

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }

    @Test
    @DisplayName("doFilterInternal(): empty Bearer token does not authenticate")
    void doFilterInternal_emptyToken_doesNotAuthenticate() throws ServletException, IOException {
        when(this.request.getHeader("Authorization")).thenReturn("Bearer ");

        this.jwtAuthFilter.doFilterInternal(this.request, this.response, this.filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(this.filterChain, times(1)).doFilter(this.request, this.response);
    }
}
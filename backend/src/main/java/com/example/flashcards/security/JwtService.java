package com.example.flashcards.security;

import com.example.flashcards.entity.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    /**
     * Generate JWT token for a user
     * @param user The user for whom the token is generated
     * @return The generated JWT token
     */
    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("role", user.getRole().name())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
            .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extract username from JWT token
     * @param token The JWT token
     * @return The username extracted from the token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(this.getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * Check if a token is expired
     * @param token The JWT token
     * @return True if the token is expired, false otherwise
     */
    public boolean isExpiredToken(String token) {
        Date expirationDate = Jwts.parserBuilder()
            .setSigningKey(this.getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
        return expirationDate.before(new Date());
    }

    /**
     * Validate a token against user details, and check expiration
     * @param token The JWT token
     * @param userDetails The user details to validate against
     * @return True if the token is valid, false otherwise
     */
    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isExpiredToken(token));
    }
}

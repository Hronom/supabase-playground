package com.example.supabase.service;

import com.example.supabase.model.SupabaseUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Validates a Supabase JWT token and extracts user information
     *
     * @param token The JWT token to validate
     * @return The user information extracted from the token
     * @throws JwtException If the token is invalid
     */
    public SupabaseUser validateToken(String token) {
        try {
            // Decode the JWT token
            SecretKey key = Keys.hmacShaKeyFor(getDecodedSecret());
            
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            
            Claims claims = claimsJws.getBody();
            
            // Extract user information from the token
            String userId = claims.getSubject();
            String email = (String) claims.get("email");
            
            // Extract app_metadata and user_metadata
            Map<String, Object> appMetadata = new HashMap<>();
            Map<String, Object> userMetadata = new HashMap<>();
            
            if (claims.containsKey("app_metadata")) {
                appMetadata = objectMapper.convertValue(claims.get("app_metadata"), Map.class);
            }
            
            if (claims.containsKey("user_metadata")) {
                userMetadata = objectMapper.convertValue(claims.get("user_metadata"), Map.class);
            }
            
            // Build and return the user object
            return SupabaseUser.builder()
                    .id(userId)
                    .email(email)
                    .createdAt(Instant.now()) // This would ideally come from the token
                    .updatedAt(Instant.now()) // This would ideally come from the token
                    .appMetadata(appMetadata)
                    .userMetadata(userMetadata)
                    .build();
        } catch (Exception e) {
            log.error("Failed to validate Supabase JWT token", e);
            throw new JwtException("Invalid Supabase JWT token", e);
        }
    }

    /**
     * Registers a new user in Supabase
     *
     * @param email The user's email
     * @param password The user's password
     * @return The newly created user
     */
    public SupabaseUser registerUser(String email, String password) {
        try {
            // Set up headers for Supabase API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Content-Type", "application/json");
            
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            
            // Make API request to Supabase Auth API
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    supabaseUrl + "/auth/v1/signup",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            
            // Parse response
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // Extract user information
            String userId = jsonNode.path("id").asText();
            String userEmail = jsonNode.path("email").asText();
            
            // Build and return the user object
            return SupabaseUser.builder()
                    .id(userId)
                    .email(userEmail)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
        } catch (Exception e) {
            log.error("Failed to register user with Supabase", e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    /**
     * Decodes the Base64-encoded JWT secret
     *
     * @return The decoded secret as a byte array
     */
    private byte[] getDecodedSecret() {
        return Base64.getDecoder().decode(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}

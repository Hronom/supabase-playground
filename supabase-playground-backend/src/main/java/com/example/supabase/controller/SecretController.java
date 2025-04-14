package com.example.supabase.controller;

import com.example.supabase.model.SupabaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secret")
@Slf4j
public class SecretController {

    /**
     * Protected endpoint that requires authentication
     * This endpoint can only be accessed by authenticated users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSecretData(Authentication authentication) {
        // Get the authenticated user
        SupabaseUser user = (SupabaseUser) authentication.getPrincipal();
        
        log.info("User {} accessed secret endpoint", user.getEmail());
        
        // Create a response with some secret data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a secret message that only authenticated users can see!");
        response.put("userId", user.getId());
        response.put("userEmail", user.getEmail());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}

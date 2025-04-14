package com.github.hronom.supabase_playground_backend.controller;

import com.github.hronom.supabase_playground_backend.model.SupabaseUser;
import com.github.hronom.supabase_playground_backend.service.SupabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final SupabaseService supabaseService;

    public AuthController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    /**
     * Endpoint for user registration
     * This is called by the frontend when a user registers with email/password
     */
    @PostMapping("/register")
    public ResponseEntity<SupabaseUser> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            SupabaseUser user = supabaseService.registerUser(email, password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error registering user", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

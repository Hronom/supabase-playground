package com.github.hronom.supabase_playground_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupabaseUser {
    private String id;
    private String email;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> appMetadata;
    private Map<String, Object> userMetadata;
}

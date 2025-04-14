package com.example.supabase.security;

import com.example.supabase.model.SupabaseUser;
import com.example.supabase.service.SupabaseService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SupabaseJwtAuthenticationProvider implements AuthenticationProvider {

    private final SupabaseService supabaseService;

    public SupabaseJwtAuthenticationProvider(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SupabaseJwtAuthenticationToken authToken = (SupabaseJwtAuthenticationToken) authentication;
        String token = (String) authToken.getCredentials();

        try {
            // Validate the JWT token and extract user information
            SupabaseUser user = supabaseService.validateToken(token);
            
            if (user != null) {
                // Create an authenticated token with the user as principal
                return new SupabaseJwtAuthenticationToken(
                        user,
                        token,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
            
            throw new BadCredentialsException("Invalid Supabase JWT token");
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to authenticate with Supabase JWT token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SupabaseJwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

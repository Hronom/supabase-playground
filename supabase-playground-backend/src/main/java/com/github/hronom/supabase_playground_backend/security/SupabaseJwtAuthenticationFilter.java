package com.github.hronom.supabase_playground_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class SupabaseJwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public SupabaseJwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        // Apply to all requests
        super(request -> true);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        // Extract the token from the Authorization header
        String token = null;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if (StringUtils.hasText(token)) {
            // Create an unauthenticated SupabaseJwtAuthenticationToken with the token
            SupabaseJwtAuthenticationToken authRequest = new SupabaseJwtAuthenticationToken(token);

            // Delegate authentication to the AuthenticationManager
            return getAuthenticationManager().authenticate(authRequest);

        } else {
            throw new InsufficientAuthenticationException("No Supabase ID token found in request headers.");
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // Proceed with the next filter in the chain
        chain.doFilter(request, response);
    }
}
